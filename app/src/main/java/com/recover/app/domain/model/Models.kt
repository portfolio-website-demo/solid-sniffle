package com.recover.app.domain.model

enum class SessionStatus {
    SETUP_DURATION,
    CAMERA_PERMISSION_REQUIRED,
    AR_ZONE_SETUP,
    LOCKING_IN_COUNTDOWN,
    READY_TO_START,
    ACTIVE,
    CONFIRMING_EARLY_END,
    MOTION_WARNING_GRACE,
    COMPLETED_NORMALLY,
    ENDED_EARLY,
    FAILED_STRIKES,
    INTERRUPTED_PERMISSION_REVOKED
}

enum class SessionEndReason {
    NONE,
    PLANNED_COMPLETION,
    USER_CONFIRMED_EARLY_END,
    THIRD_STRIKE_FORFEIT,
    PERMISSION_REVOKED,
    EMERGENCY_EXIT
}

enum class RewardStatus {
    PENDING,
    BANKED,
    ZERO_EARLY_END,
    ZERO_FORFEITED
}

data class FocusSession(
    val id: String,
    val plannedDurationMinutes: Int,
    val startEpochMs: Long,
    val startElapsedRealtimeMs: Long,
    val endEpochMs: Long? = null,
    val status: SessionStatus,
    val endReason: SessionEndReason = SessionEndReason.NONE,
    val rewardStatus: RewardStatus = RewardStatus.PENDING,
    val creditsEarned: Int = 0,
    val strikes: Int = 0
)

enum class TransactionType {
    WELCOME_BONUS,
    FOCUS_SESSION_REWARD,
    APP_UNLOCK_DEBIT
}

data class CreditTransaction(
    val id: String,
    val signedDelta: Int,
    val type: TransactionType,
    val title: String,
    val subtitle: String,
    val relatedSessionId: String? = null,
    val relatedUnlockId: String? = null,
    val relatedPackageName: String? = null,
    val idempotencyKey: String,
    val createdAtEpochMs: Long
)

enum class AppCategory(val displayName: String) {
    SOCIAL("Social"),
    VIDEO_ENTERTAINMENT("Video & Media"),
    GAMES("Games"),
    PRODUCTIVITY_OTHER("Other Apps")
}

data class BlockedApp(
    val packageName: String,
    val label: String,
    val category: AppCategory,
    val selected: Boolean,
    val updatedAtEpochMs: Long = System.currentTimeMillis()
)

enum class UnlockGrantState {
    ACTIVE,
    EXPIRED,
    REVOKED
}

data class UnlockGrant(
    val id: String,
    val packageName: String,
    val startEpochMs: Long,
    val expiryEpochMs: Long,
    val startElapsedRealtimeMs: Long,
    val expiryElapsedRealtimeMs: Long,
    val debitTransactionId: String,
    val idempotencyKey: String,
    val state: UnlockGrantState
) {
    fun isCurrentlyValid(nowElapsedRealtimeMs: Long, nowEpochMs: Long): Boolean {
        if (state != UnlockGrantState.ACTIVE) return false
        val elapsedValid = if (startElapsedRealtimeMs > 0L && nowElapsedRealtimeMs >= startElapsedRealtimeMs) {
            nowElapsedRealtimeMs < expiryElapsedRealtimeMs
        } else {
            true
        }
        val wallValid = nowEpochMs < expiryEpochMs
        return elapsedValid && wallValid
    }
}

enum class PermissionCapability {
    ACCESSIBILITY_ENFORCEMENT,
    NOTIFICATIONS,
    OVERLAY_DISPLAY,
    CAMERA_AR_PRESENCE
}

data class PermissionStatus(
    val capability: PermissionCapability,
    val isGranted: Boolean,
    val checkedAtEpochMs: Long
)

data class UserSettings(
    val hasCompletedOnboarding: Boolean = false,
    val displayName: String = "JJ",
    val handle: String = "@focus_champ",
    val cardNumber: String = "0116931",
    val isAuthenticated: Boolean = false,
    val authEmail: String? = null,
    val isProSubscriber: Boolean = false
)
