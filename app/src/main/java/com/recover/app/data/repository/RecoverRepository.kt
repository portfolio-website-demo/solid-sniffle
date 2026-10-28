package com.recover.app.data.repository

import androidx.room.withTransaction
import com.recover.app.data.local.BlockedAppEntity
import com.recover.app.data.local.CreditTransactionEntity
import com.recover.app.data.local.FocusSessionEntity
import com.recover.app.data.local.RecoverDatabase
import com.recover.app.data.local.UnlockGrantEntity
import com.recover.app.domain.model.BlockedApp
import com.recover.app.domain.model.CreditTransaction
import com.recover.app.domain.model.FocusSession
import com.recover.app.domain.model.SessionEndReason
import com.recover.app.domain.model.SessionStatus
import com.recover.app.domain.model.TransactionType
import com.recover.app.domain.model.UnlockGrant
import com.recover.app.domain.model.UnlockGrantState
import com.recover.app.domain.rules.FocusRewardRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

sealed class UnlockResult {
    data class Success(val grant: UnlockGrant, val alreadyProcessed: Boolean = false) : UnlockResult()
    data object AppNotSelectedAsBlocked : UnlockResult()
    data object InsufficientCredits : UnlockResult()
    data class Failure(val reason: String) : UnlockResult()
}

class RecoverRepository(private val database: RecoverDatabase) {
    private val dao = database.recoverDao()

    val balanceFlow: Flow<Int> = dao.observeBalance()

    val transactionsFlow: Flow<List<CreditTransaction>> =
        dao.observeTransactions().map { list -> list.map { it.toDomain() } }

    val sessionsFlow: Flow<List<FocusSession>> =
        dao.observeSessions().map { list -> list.map { it.toDomain() } }

    val blockedAppsFlow: Flow<List<BlockedApp>> =
        dao.observeBlockedApps().map { list -> list.map { it.toDomain() } }

    val selectedBlockedAppsFlow: Flow<List<BlockedApp>> =
        dao.observeSelectedBlockedApps().map { list -> list.map { it.toDomain() } }

    val activeUnlockGrantsFlow: Flow<List<UnlockGrant>> =
        dao.observeActiveUnlockGrants().map { list -> list.map { it.toDomain() } }

    suspend fun saveBlockedAppsSelection(apps: List<BlockedApp>) {
        dao.saveSelectedBlockedApps(apps.map { BlockedAppEntity.fromDomain(it) })
    }

    suspend fun getSelectedBlockedPackageSet(): Set<String> {
        return dao.getSelectedBlockedPackages().toSet()
    }

    /**
     * Persists a finalized FocusSession and, ONLY if it completed its planned duration normally,
     * inserts an idempotent positive CreditTransaction in a single atomic database transaction.
     * Confirmed early-end or 3rd-strike sessions record 0 credits and never insert a credit reward.
     */
    suspend fun recordSessionOutcomeIdempotently(
        session: FocusSession,
        nowEpochMs: Long
    ): Int {
        return database.withTransaction {
            val existingSession = dao.getSessionById(session.id)
            if (existingSession != null &&
                (existingSession.status == SessionStatus.COMPLETED_NORMALLY.name ||
                    existingSession.status == SessionStatus.ENDED_EARLY.name ||
                    existingSession.status == SessionStatus.FAILED_STRIKES.name)
            ) {
                return@withTransaction existingSession.creditsEarned
            }

            dao.upsertSession(FocusSessionEntity.fromDomain(session))

            if (session.status == SessionStatus.COMPLETED_NORMALLY &&
                session.endReason == SessionEndReason.PLANNED_COMPLETION &&
                session.creditsEarned > 0
            ) {
                val idempotencyKey = "session_reward:${session.id}"
                val existingTx = dao.getTransactionByIdempotencyKey(idempotencyKey)
                if (existingTx == null) {
                    dao.insertTransaction(
                        CreditTransactionEntity(
                            id = UUID.randomUUID().toString(),
                            signedDelta = session.creditsEarned,
                            type = TransactionType.FOCUS_SESSION_REWARD.name,
                            title = "Focus Session Completed",
                            subtitle = "${session.plannedDurationMinutes}m focus session reward",
                            relatedSessionId = session.id,
                            relatedUnlockId = null,
                            relatedPackageName = null,
                            idempotencyKey = idempotencyKey,
                            createdAtEpochMs = nowEpochMs
                        )
                    )
                }
                session.creditsEarned
            } else {
                0
            }
        }
    }

    /**
     * Atomically debits 1 credit and creates a 60-second UnlockGrant for a single selected blocked package.
     * - Retries with the same idempotencyKey never double-charge.
     * - Insufficient balance or unselected package never charges.
     */
    suspend fun requestSixtySecondAppUnlock(
        packageName: String,
        appLabel: String,
        idempotencyKey: String,
        nowEpochMs: Long,
        nowElapsedRealtimeMs: Long
    ): UnlockResult {
        return try {
            database.withTransaction {
                val existingGrant = dao.getUnlockGrantByIdempotencyKey(idempotencyKey)
                if (existingGrant != null) {
                    return@withTransaction UnlockResult.Success(
                        grant = existingGrant.toDomain(),
                        alreadyProcessed = true
                    )
                }

                val selectedPackages = dao.getSelectedBlockedPackages().toSet()
                if (packageName !in selectedPackages) {
                    return@withTransaction UnlockResult.AppNotSelectedAsBlocked
                }

                val currentBalance = dao.getCurrentBalance()
                if (currentBalance < FocusRewardRules.UNLOCK_COST_CREDITS) {
                    return@withTransaction UnlockResult.InsufficientCredits
                }

                val txId = UUID.randomUUID().toString()
                val grantId = UUID.randomUUID().toString()

                dao.insertTransaction(
                    CreditTransactionEntity(
                        id = txId,
                        signedDelta = -FocusRewardRules.UNLOCK_COST_CREDITS,
                        type = TransactionType.APP_UNLOCK_DEBIT.name,
                        title = "App Unlock: $appLabel",
                        subtitle = "60-second temporary unlock (-1 credit)",
                        relatedSessionId = null,
                        relatedUnlockId = grantId,
                        relatedPackageName = packageName,
                        idempotencyKey = idempotencyKey,
                        createdAtEpochMs = nowEpochMs
                    )
                )

                val grantEntity = UnlockGrantEntity(
                    id = grantId,
                    packageName = packageName,
                    startEpochMs = nowEpochMs,
                    expiryEpochMs = nowEpochMs + FocusRewardRules.UNLOCK_DURATION_MS,
                    startElapsedRealtimeMs = nowElapsedRealtimeMs,
                    expiryElapsedRealtimeMs = nowElapsedRealtimeMs + FocusRewardRules.UNLOCK_DURATION_MS,
                    debitTransactionId = txId,
                    idempotencyKey = idempotencyKey,
                    state = UnlockGrantState.ACTIVE.name
                )
                dao.insertUnlockGrant(grantEntity)

                UnlockResult.Success(grant = grantEntity.toDomain(), alreadyProcessed = false)
            }
        } catch (e: Exception) {
            UnlockResult.Failure(e.message ?: "Failed to unlock app")
        }
    }

    suspend fun isPackageTemporarilyUnlocked(
        packageName: String,
        nowElapsedRealtimeMs: Long,
        nowEpochMs: Long
    ): Boolean {
        dao.expireElapsedUnlockGrants(nowEpochMs)
        val grant = dao.getActiveUnlockGrantForPackage(packageName)?.toDomain() ?: return false
        val valid = grant.isCurrentlyValid(nowElapsedRealtimeMs, nowEpochMs)
        if (!valid) {
            dao.markUnlockGrantExpired(grant.id)
        }
        return valid
    }
}
