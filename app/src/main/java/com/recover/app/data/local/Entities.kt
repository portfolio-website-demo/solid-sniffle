package com.recover.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.recover.app.domain.model.AppCategory
import com.recover.app.domain.model.BlockedApp
import com.recover.app.domain.model.CreditTransaction
import com.recover.app.domain.model.FocusSession
import com.recover.app.domain.model.RewardStatus
import com.recover.app.domain.model.SessionEndReason
import com.recover.app.domain.model.SessionStatus
import com.recover.app.domain.model.TransactionType
import com.recover.app.domain.model.UnlockGrant
import com.recover.app.domain.model.UnlockGrantState

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String,
    val plannedDurationMinutes: Int,
    val startEpochMs: Long,
    val startElapsedRealtimeMs: Long,
    val endEpochMs: Long?,
    val status: String,
    val endReason: String,
    val rewardStatus: String,
    val creditsEarned: Int,
    val strikes: Int
) {
    fun toDomain(): FocusSession = FocusSession(
        id = id,
        plannedDurationMinutes = plannedDurationMinutes,
        startEpochMs = startEpochMs,
        startElapsedRealtimeMs = startElapsedRealtimeMs,
        endEpochMs = endEpochMs,
        status = SessionStatus.valueOf(status),
        endReason = SessionEndReason.valueOf(endReason),
        rewardStatus = RewardStatus.valueOf(rewardStatus),
        creditsEarned = creditsEarned,
        strikes = strikes
    )

    companion object {
        fun fromDomain(session: FocusSession): FocusSessionEntity = FocusSessionEntity(
            id = session.id,
            plannedDurationMinutes = session.plannedDurationMinutes,
            startEpochMs = session.startEpochMs,
            startElapsedRealtimeMs = session.startElapsedRealtimeMs,
            endEpochMs = session.endEpochMs,
            status = session.status.name,
            endReason = session.endReason.name,
            rewardStatus = session.rewardStatus.name,
            creditsEarned = session.creditsEarned,
            strikes = session.strikes
        )
    }
}

@Entity(
    tableName = "credit_transactions",
    indices = [Index(value = ["idempotencyKey"], unique = true)]
)
data class CreditTransactionEntity(
    @PrimaryKey val id: String,
    val signedDelta: Int,
    val type: String,
    val title: String,
    val subtitle: String,
    val relatedSessionId: String?,
    val relatedUnlockId: String?,
    val relatedPackageName: String?,
    val idempotencyKey: String,
    val createdAtEpochMs: Long
) {
    fun toDomain(): CreditTransaction = CreditTransaction(
        id = id,
        signedDelta = signedDelta,
        type = TransactionType.valueOf(type),
        title = title,
        subtitle = subtitle,
        relatedSessionId = relatedSessionId,
        relatedUnlockId = relatedUnlockId,
        relatedPackageName = relatedPackageName,
        idempotencyKey = idempotencyKey,
        createdAtEpochMs = createdAtEpochMs
    )
}

@Entity(tableName = "blocked_apps")
data class BlockedAppEntity(
    @PrimaryKey val packageName: String,
    val label: String,
    val category: String,
    val selected: Boolean,
    val updatedAtEpochMs: Long
) {
    fun toDomain(): BlockedApp = BlockedApp(
        packageName = packageName,
        label = label,
        category = runCatching { AppCategory.valueOf(category) }.getOrDefault(AppCategory.PRODUCTIVITY_OTHER),
        selected = selected,
        updatedAtEpochMs = updatedAtEpochMs
    )

    companion object {
        fun fromDomain(app: BlockedApp): BlockedAppEntity = BlockedAppEntity(
            packageName = app.packageName,
            label = app.label,
            category = app.category.name,
            selected = app.selected,
            updatedAtEpochMs = app.updatedAtEpochMs
        )
    }
}

@Entity(
    tableName = "unlock_grants",
    indices = [
        Index(value = ["packageName"]),
        Index(value = ["idempotencyKey"], unique = true)
    ]
)
data class UnlockGrantEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val startEpochMs: Long,
    val expiryEpochMs: Long,
    val startElapsedRealtimeMs: Long,
    val expiryElapsedRealtimeMs: Long,
    val debitTransactionId: String,
    val idempotencyKey: String,
    val state: String
) {
    fun toDomain(): UnlockGrant = UnlockGrant(
        id = id,
        packageName = packageName,
        startEpochMs = startEpochMs,
        expiryEpochMs = expiryEpochMs,
        startElapsedRealtimeMs = startElapsedRealtimeMs,
        expiryElapsedRealtimeMs = expiryElapsedRealtimeMs,
        debitTransactionId = debitTransactionId,
        idempotencyKey = idempotencyKey,
        state = UnlockGrantState.valueOf(state)
    )
}
