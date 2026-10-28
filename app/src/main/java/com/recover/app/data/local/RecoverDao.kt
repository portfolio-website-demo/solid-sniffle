package com.recover.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecoverDao {

    // --- Ledger (Source of Truth for Credits) ---

    @Query("SELECT COALESCE(SUM(signedDelta), 0) FROM credit_transactions")
    fun observeBalance(): Flow<Int>

    @Query("SELECT COALESCE(SUM(signedDelta), 0) FROM credit_transactions")
    suspend fun getCurrentBalance(): Int

    @Query("SELECT * FROM credit_transactions ORDER BY createdAtEpochMs DESC")
    fun observeTransactions(): Flow<List<CreditTransactionEntity>>

    @Query("SELECT * FROM credit_transactions WHERE idempotencyKey = :key LIMIT 1")
    suspend fun getTransactionByIdempotencyKey(key: String): CreditTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: CreditTransactionEntity)

    // --- Focus Sessions ---

    @Query("SELECT * FROM focus_sessions ORDER BY startEpochMs DESC")
    fun observeSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions ORDER BY startEpochMs DESC")
    suspend fun getAllSessions(): List<FocusSessionEntity>

    @Query("SELECT * FROM focus_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: String): FocusSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: FocusSessionEntity)

    @Update
    suspend fun updateSession(session: FocusSessionEntity)

    // --- Blocked Distracting Apps ---

    @Query("SELECT * FROM blocked_apps ORDER BY label ASC")
    fun observeBlockedApps(): Flow<List<BlockedAppEntity>>

    @Query("SELECT * FROM blocked_apps WHERE selected = 1 ORDER BY label ASC")
    fun observeSelectedBlockedApps(): Flow<List<BlockedAppEntity>>

    @Query("SELECT packageName FROM blocked_apps WHERE selected = 1")
    suspend fun getSelectedBlockedPackages(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBlockedApps(apps: List<BlockedAppEntity>)

    @Query("UPDATE blocked_apps SET selected = 0")
    suspend fun clearAllBlockedAppSelections()

    @Transaction
    suspend fun saveSelectedBlockedApps(apps: List<BlockedAppEntity>) {
        clearAllBlockedAppSelections()
        upsertBlockedApps(apps)
    }

    // --- Temporary 60-Second Unlock Grants ---

    @Query("SELECT * FROM unlock_grants WHERE idempotencyKey = :key LIMIT 1")
    suspend fun getUnlockGrantByIdempotencyKey(key: String): UnlockGrantEntity?

    @Query("SELECT * FROM unlock_grants WHERE packageName = :packageName AND state = 'ACTIVE' ORDER BY expiryEpochMs DESC LIMIT 1")
    suspend fun getActiveUnlockGrantForPackage(packageName: String): UnlockGrantEntity?

    @Query("SELECT * FROM unlock_grants WHERE state = 'ACTIVE'")
    fun observeActiveUnlockGrants(): Flow<List<UnlockGrantEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUnlockGrant(grant: UnlockGrantEntity)

    @Query("UPDATE unlock_grants SET state = 'EXPIRED' WHERE id = :grantId")
    suspend fun markUnlockGrantExpired(grantId: String)

    @Query("UPDATE unlock_grants SET state = 'EXPIRED' WHERE state = 'ACTIVE' AND expiryEpochMs <= :nowEpochMs")
    suspend fun expireElapsedUnlockGrants(nowEpochMs: Long): Int
}
