package com.recover.app.domain.rules

import com.recover.app.domain.model.FocusSession
import com.recover.app.domain.model.RewardStatus
import com.recover.app.domain.model.SessionEndReason
import com.recover.app.domain.model.SessionStatus

enum class CompletionRouteDestination {
    NONE,
    NORMAL_COMPLETION_ROUTE,
    EARLY_END_FRAME_012_ROUTE,
    STRIKE_FORFEIT_ROUTE
}

data class SessionTransitionResult(
    val session: FocusSession,
    val routeDestination: CompletionRouteDestination = CompletionRouteDestination.NONE,
    val stateChanged: Boolean = true
)

object FocusSessionStateMachine {

    fun onUserTapEndSession(current: FocusSession): SessionTransitionResult {
        if (current.status != SessionStatus.ACTIVE && current.status != SessionStatus.MOTION_WARNING_GRACE) {
            return SessionTransitionResult(current, CompletionRouteDestination.NONE, stateChanged = false)
        }
        return SessionTransitionResult(
            session = current.copy(status = SessionStatus.CONFIRMING_EARLY_END),
            routeDestination = CompletionRouteDestination.NONE,
            stateChanged = true
        )
    }

    fun onUserCancelEarlyEnd(current: FocusSession): SessionTransitionResult {
        if (current.status != SessionStatus.CONFIRMING_EARLY_END) {
            return SessionTransitionResult(current, CompletionRouteDestination.NONE, stateChanged = false)
        }
        return SessionTransitionResult(
            session = current.copy(status = SessionStatus.ACTIVE),
            routeDestination = CompletionRouteDestination.NONE,
            stateChanged = true
        )
    }

    /**
     * Explicit user-confirmed early end:
     * - Finalizes early exactly once.
     * - Awards 0 credits under all-or-nothing policy.
     * - Routes exclusively to EARLY_END_FRAME_012_ROUTE (native screen matching ezgif-frame-012.jpg).
     */
    fun onUserConfirmEarlyEnd(
        current: FocusSession,
        nowEpochMs: Long
    ): SessionTransitionResult {
        if (current.status == SessionStatus.ENDED_EARLY) {
            // Idempotent no-op if already finalized early
            return SessionTransitionResult(
                session = current,
                routeDestination = CompletionRouteDestination.EARLY_END_FRAME_012_ROUTE,
                stateChanged = false
            )
        }
        if (current.status != SessionStatus.CONFIRMING_EARLY_END && current.status != SessionStatus.ACTIVE) {
            return SessionTransitionResult(current, CompletionRouteDestination.NONE, stateChanged = false)
        }
        val finalized = current.copy(
            status = SessionStatus.ENDED_EARLY,
            endEpochMs = nowEpochMs,
            endReason = SessionEndReason.USER_CONFIRMED_EARLY_END,
            rewardStatus = RewardStatus.ZERO_EARLY_END,
            creditsEarned = 0
        )
        return SessionTransitionResult(
            session = finalized,
            routeDestination = CompletionRouteDestination.EARLY_END_FRAME_012_ROUTE,
            stateChanged = true
        )
    }

    /**
     * Natural timer completion:
     * - Uses NORMAL_COMPLETION_ROUTE (never EARLY_END_FRAME_012_ROUTE).
     * - Calculates all-or-nothing reward once.
     */
    fun onNaturalTimerExpiry(
        current: FocusSession,
        nowEpochMs: Long,
        nowElapsedRealtimeMs: Long
    ): SessionTransitionResult {
        if (current.status == SessionStatus.COMPLETED_NORMALLY) {
            return SessionTransitionResult(
                session = current,
                routeDestination = CompletionRouteDestination.NORMAL_COMPLETION_ROUTE,
                stateChanged = false
            )
        }
        if (current.status != SessionStatus.ACTIVE && current.status != SessionStatus.CONFIRMING_EARLY_END) {
            return SessionTransitionResult(current, CompletionRouteDestination.NONE, stateChanged = false)
        }
        val elapsedMs = (nowElapsedRealtimeMs - current.startElapsedRealtimeMs).coerceAtLeast(0L)
        val credits = FocusRewardRules.calculateSessionReward(
            plannedDurationMinutes = current.plannedDurationMinutes,
            actualElapsedMs = elapsedMs,
            status = SessionStatus.COMPLETED_NORMALLY,
            endReason = SessionEndReason.PLANNED_COMPLETION,
            strikes = current.strikes
        )
        val finalized = current.copy(
            status = SessionStatus.COMPLETED_NORMALLY,
            endEpochMs = nowEpochMs,
            endReason = SessionEndReason.PLANNED_COMPLETION,
            rewardStatus = if (credits > 0) RewardStatus.BANKED else RewardStatus.ZERO_FORFEITED,
            creditsEarned = credits
        )
        return SessionTransitionResult(
            session = finalized,
            routeDestination = CompletionRouteDestination.NORMAL_COMPLETION_ROUTE,
            stateChanged = true
        )
    }

    /**
     * Strike recording during active session (per ezgif-frame-010.jpg: "forfeit on 3rd strike").
     */
    fun onStrikeRecorded(
        current: FocusSession,
        nowEpochMs: Long
    ): SessionTransitionResult {
        if (current.status != SessionStatus.ACTIVE && current.status != SessionStatus.MOTION_WARNING_GRACE) {
            return SessionTransitionResult(current, CompletionRouteDestination.NONE, stateChanged = false)
        }
        val newStrikes = current.strikes + 1
        return if (newStrikes >= FocusRewardRules.MAX_STRIKES) {
            val forfeited = current.copy(
                strikes = newStrikes,
                status = SessionStatus.FAILED_STRIKES,
                endEpochMs = nowEpochMs,
                endReason = SessionEndReason.THIRD_STRIKE_FORFEIT,
                rewardStatus = RewardStatus.ZERO_FORFEITED,
                creditsEarned = 0
            )
            SessionTransitionResult(
                session = forfeited,
                routeDestination = CompletionRouteDestination.STRIKE_FORFEIT_ROUTE,
                stateChanged = true
            )
        } else {
            SessionTransitionResult(
                session = current.copy(strikes = newStrikes, status = SessionStatus.ACTIVE),
                routeDestination = CompletionRouteDestination.NONE,
                stateChanged = true
            )
        }
    }
}
