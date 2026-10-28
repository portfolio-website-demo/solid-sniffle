package com.recover.app

import android.app.Application
import com.recover.app.data.local.RecoverDatabase
import com.recover.app.data.preferences.UserPreferencesRepository
import com.recover.app.data.repository.RecoverRepository

class RecoverApplication : Application() {
    val database: RecoverDatabase by lazy { RecoverDatabase.getInstance(this) }
    val repository: RecoverRepository by lazy { RecoverRepository(database) }
    val preferencesRepository: UserPreferencesRepository by lazy { UserPreferencesRepository(this) }
}
