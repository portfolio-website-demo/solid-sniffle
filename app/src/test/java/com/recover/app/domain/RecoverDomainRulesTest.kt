package com.recover.app.domain

import com.recover.app.domain.model.FocusSession
import com.recover.app.domain.model.RewardStatus
import com.recover.app.domain.model.SessionEndReason
import com.recover.app.domain.model.SessionStatus
import com.recover.app.domain.model.UnlockGrant
import com.recover.app.domain.model.UnlockGrantState
import com.recover.app.domain.rules.CompletionRouteDestination
import com.recover.app.domain.rules.FocusRewardRules
import com.recover.app.domain.rules.FocusSessionStateMachine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneOffset

class RecoverDomainRulesTest {

    private fun createActiveSession(
        plannedMinutes: Int = 25,
        startEpochMs: Long = 1_700_000_000_000L,
        startElapsedMs: Long = 10_000L,
        strikes: Int = 0
    ): FocusSession = FocusSession(
        id = "session-1",
        plannedDurationMinutes = plannedMinutes,
        startEpochMs = startEpochMs,
        startElapsedRealtimeMs = startElapsedMs,
        status = SessionStatus.ACTIVE,
        strikes = strikes
    )

    @Test
    fun `all-or-nothing reward grants 1 credit per full planned minute on normal completion`() {
        val credits = FocusRewardRules.calculateSessionReward(
            plannedDurationMinutes = 25,
            actualElapsedMs = 25 * 60_000L,
            status = SessionStatus.COMPLETED_NORMALLY,
            endReason = SessionEndReason.PLANNED_COMPLETION,
            strikes = 1
        )
        assertEquals(25, credits)
    }

    @Test
    fun `all-or-nothing reward grants zero credits on confirmed early end`() {
        val credits = FocusRewardRules.calculateSessionReward(
            plannedDurationMinutes = 25,
            actualElapsedMs = 24 * 60_000L,
            status = SessionStatus.ENDED_EARLY,
            endReason = SessionEndReason.USER_CONFIRMED_EARLY_END,
            strikes = 0
        )
        assertEquals(0, credits)
    }

    @Test
    fun `early end cancel keeps session active without finalizing or routing to frame 012`() {
        val active = createActiveSession(plannedMinutes = 20)
        val confirming = FocusSessionStateMachine.onUserTapEndSession(active)
        assertEquals(SessionStatus.CONFIRMING_EARLY_END, confirming.session.status)
        assertEquals(CompletionRouteDestination.NONE, confirming.routeDestination)

        val cancelled = FocusSessionStateMachine.onUserCancelEarlyEnd(confirming.session)
        assertEquals(SessionStatus.ACTIVE, cancelled.session.status)
        assertEquals(CompletionRouteDestination.NONE, cancelled.routeDestination)
        assertEquals(0, cancelled.session.creditsEarned)
    }

    @Test
    fun `early end confirm finalizes once with zero credits and routes to frame 012 screen`() {
        val active = createActiveSession(plannedMinutes = 30)
        val confirming = FocusSessionStateMachine.onUserTapEndSession(active)

        val confirmed = FocusSessionStateMachine.onUserConfirmEarlyEnd(
            current = confirming.session,
            nowEpochMs = active.startEpochMs + 10 * 60_000L
        )
        assertTrue(confirmed.stateChanged)
        assertEquals(SessionStatus.ENDED_EARLY, confirmed.session.status)
        assertEquals(SessionEndReason.USER_CONFIRMED_EARLY_END, confirmed.session.endReason)
        assertEquals(RewardStatus.ZERO_EARLY_END, confirmed.session.rewardStatus)
        assertEquals(0, confirmed.session.creditsEarned)
        assertEquals(CompletionRouteDestination.EARLY_END_FRAME_012_ROUTE, confirmed.routeDestination)

        // Duplicate confirm tap must be idempotent no-op
        val duplicateConfirm = FocusSessionStateMachine.onUserConfirmEarlyEnd(
            current = confirmed.session,
            nowEpochMs = active.startEpochMs + 11 * 60_000L
        )
        assertFalse(duplicateConfirm.stateChanged)
        assertEquals(confirmed.session, duplicateConfirm.session)
        assertEquals(CompletionRouteDestination.EARLY_END_FRAME_012_ROUTE, duplicateConfirm.routeDestination)
    }

    @Test
    fun `natural timer expiry routes to normal completion route and never frame 012 route`() {
        val active = createActiveSession(plannedMinutes = 20, startElapsedMs = 1_000L)
        val completed = FocusSessionStateMachine.onNaturalTimerExpiry(
            current = active,
            nowEpochMs = active.startEpochMs + 20 * 60_000L,
            nowElapsedRealtimeMs = 1_000L + 20 * 60_000L
        )
        assertTrue(completed.stateChanged)
        assertEquals(SessionStatus.COMPLETED_NORMALLY, completed.session.status)
        assertEquals(20, completed.session.creditsEarned)
        assertEquals(CompletionRouteDestination.NORMAL_COMPLETION_ROUTE, completed.routeDestination)
    }

    @Test
    fun `third strike forfeits session with zero credits and strike forfeit route`() {
        var session = createActiveSession(plannedMinutes = 25)
        val strike1 = FocusSessionStateMachine.onStrikeRecorded(session, nowEpochMs = 100L)
        assertEquals(1, strike1.session.strikes)
        assertEquals(SessionStatus.ACTIVE, strike1.session.status)

        val strike2 = FocusSessionStateMachine.onStrikeRecorded(strike1.session, nowEpochMs = 200L)
        assertEquals(2, strike2.session.strikes)
        assertEquals(SessionStatus.ACTIVE, strike2.session.status)

        val strike3 = FocusSessionStateMachine.onStrikeRecorded(strike2.session, nowEpochMs = 300L)
        assertEquals(3, strike3.session.strikes)
        assertEquals(SessionStatus.FAILED_STRIKES, strike3.session.status)
        assertEquals(SessionEndReason.THIRD_STRIKE_FORFEIT, strike3.session.endReason)
        assertEquals(0, strike3.session.creditsEarned)
        assertEquals(CompletionRouteDestination.STRIKE_FORFEIT_ROUTE, strike3.routeDestination)
    }

    @Test
    fun `unlock grant expires strictly at 60 seconds`() {
        val startEpoch = 1_700_000_000_000L
        val startElapsed = 50_000L
        val grant = UnlockGrant(
            id = "grant-1",
            packageName = "com.example.social",
            startEpochMs = startEpoch,
            expiryEpochMs = startEpoch + FocusRewardRules.UNLOCK_DURATION_MS,
            startElapsedRealtimeMs = startElapsed,
            expiryElapsedRealtimeMs = startElapsed + FocusRewardRules.UNLOCK_DURATION_MS,
            debitTransactionId = "tx-1",
            idempotencyKey = "unlock:1",
            state = UnlockGrantState.ACTIVE
        )

        assertTrue(grant.isCurrentlyValid(nowElapsedRealtimeMs = startElapsed + 59_999L, nowEpochMs = startEpoch + 59_999L))
        assertFalse(grant.isCurrentlyValid(nowElapsedRealtimeMs = startElapsed + 60_000L, nowEpochMs = startEpoch + 60_000L))
    }

    @Test
    fun `peak focus analytics threshold requires 21 history days, 10 completed sessions, and 7 distinct days`() {
        val dayMs = 24 * 60 * 60 * 1000L
        val baseEpoch = 1_700_000_000_000L

        // Build 10 completed sessions across 8 distinct days spanning 22 calendar days
        val qualifyingSessions = (0 until 10).map { idx ->
            val dayOffset = if (idx < 7) idx * 3L else 21L
            FocusSession(
                id = "s-$idx",
                plannedDurationMinutes = 20,
                startEpochMs = baseEpoch + dayOffset * dayMs,
                startElapsedRealtimeMs = 1_000L,
                endEpochMs = baseEpoch + dayOffset * dayMs + 20 * 60_000L,
                status = SessionStatus.COMPLETED_NORMALLY,
                endReason = SessionEndReason.PLANNED_COMPLETION,
                rewardStatus = RewardStatus.BANKED,
                creditsEarned = 20
            )
        }

        val eligibleResult = FocusRewardRules.hasSufficientDataForPeakFocusAnalytics(
            sessions = qualifyingSessions,
            nowEpochMs = baseEpoch + 21L * dayMs,
            zoneId = ZoneOffset.UTC
        )
        assertTrue(eligibleResult.isEligible)
        assertEquals(10, eligibleResult.completedSessionsCount)

        // Insufficient history span (only 10 days) must return false
        val shortHistorySessions = qualifyingSessions.mapIndexed { idx, s ->
            s.copy(startEpochMs = baseEpoch + (idx % 8) * dayMs)
        }
        val shortHistoryResult = FocusRewardRules.hasSufficientDataForPeakFocusAnalytics(
            sessions = shortHistorySessions,
            nowEpochMs = baseEpoch + 10L * dayMs,
            zoneId = ZoneOffset.UTC
        )
        assertFalse(shortHistoryResult.isEligible)
    }
}
