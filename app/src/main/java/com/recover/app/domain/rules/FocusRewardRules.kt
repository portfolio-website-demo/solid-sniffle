package com.recover.app.domain.rules

import com.recover.app.domain.model.FocusSession
import com.recover.app.domain.model.SessionEndReason
import com.recover.app.domain.model.SessionStatus
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object FocusRewardRules {
    const val CREDITS_PER_QUALIFYING_MINUTE = 1
    const val UNLOCK_COST_CREDITS = 1
    const val UNLOCK_DURATION_SECONDS = 60L
    const val UNLOCK_DURATION_MS = UNLOCK_DURATION_SECONDS * 1000L
    const val MAX_STRIKES = 3

    const val MIN_ANALYTICS_HISTORY_DAYS = 21L
    const val MIN_ANALYTICS_COMPLETED_SESSIONS = 10
    const val MIN_ANALYTICS_DISTINCT_DAYS = 7

    /**
     * All-or-nothing reward policy:
     * - Only a session that completes its planned duration normally earns credits (1 credit per full planned minute).
     * - User-confirmed early end earns 0 credits.
     * - Third-strike forfeiture or interruption earns 0 credits.
     */
    fun calculateSessionReward(
        plannedDurationMinutes: Int,
        actualElapsedMs: Long,
        status: SessionStatus,
        endReason: SessionEndReason,
        strikes: Int
    ): Int {
        if (plannedDurationMinutes <= 0) return 0
        if (strikes >= MAX_STRIKES) return 0
        if (status != SessionStatus.COMPLETED_NORMALLY) return 0
        if (endReason != SessionEndReason.PLANNED_COMPLETION) return 0

        val plannedDurationMs = plannedDurationMinutes * 60_000L
        if (actualElapsedMs < plannedDurationMs) return 0

        return plannedDurationMinutes * CREDITS_PER_QUALIFYING_MINUTE
    }

    /**
     * Peak-focus analytics threshold check:
     * Requires at least 21 calendar days of usable history, at least 10 completed sessions,
     * and completed sessions across at least 7 distinct calendar days.
     */
    fun hasSufficientDataForPeakFocusAnalytics(
        sessions: List<FocusSession>,
        nowEpochMs: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): PeakFocusEligibility {
        if (sessions.isEmpty()) {
            return PeakFocusEligibility(
                isEligible = false,
                usableHistoryDays = 0,
                completedSessionsCount = 0,
                distinctCompletedDays = 0
            )
        }

        val earliestStartMs = sessions.minOf { it.startEpochMs }
        val earliestDate = Instant.ofEpochMilli(earliestStartMs).atZone(zoneId).toLocalDate()
        val currentDate = Instant.ofEpochMilli(nowEpochMs).atZone(zoneId).toLocalDate()
        val historySpanDays = ChronoUnit.DAYS.between(earliestDate, currentDate) + 1L

        val completedSessions = sessions.filter {
            it.status == SessionStatus.COMPLETED_NORMALLY &&
                it.endReason == SessionEndReason.PLANNED_COMPLETION
        }

        val distinctDays = completedSessions
            .map { Instant.ofEpochMilli(it.startEpochMs).atZone(zoneId).toLocalDate() }
            .toSet()
            .size

        val eligible = historySpanDays >= MIN_ANALYTICS_HISTORY_DAYS &&
            completedSessions.size >= MIN_ANALYTICS_COMPLETED_SESSIONS &&
            distinctDays >= MIN_ANALYTICS_DISTINCT_DAYS

        return PeakFocusEligibility(
            isEligible = eligible,
            usableHistoryDays = historySpanDays.toInt().coerceAtLeast(0),
            completedSessionsCount = completedSessions.size,
            distinctCompletedDays = distinctDays
        )
    }
}

data class PeakFocusEligibility(
    val isEligible: Boolean,
    val usableHistoryDays: Int,
    val completedSessionsCount: Int,
    val distinctCompletedDays: Int
)
