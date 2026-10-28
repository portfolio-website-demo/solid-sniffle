package com.recover.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FocusSessionEntity::class,
        CreditTransactionEntity::class,
        BlockedAppEntity::class,
        UnlockGrantEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RecoverDatabase : RoomDatabase() {
    abstract fun recoverDao(): RecoverDao

    companion object {
        @Volatile
        private var INSTANCE: RecoverDatabase? = null

        fun getInstance(context: Context): RecoverDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecoverDatabase::class.java,
                    "recover_app.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
