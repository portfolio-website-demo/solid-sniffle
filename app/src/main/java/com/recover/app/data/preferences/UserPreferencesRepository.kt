package com.recover.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.recover.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recover_user_settings")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val HANDLE = stringPreferencesKey("handle")
        val CARD_NUMBER = stringPreferencesKey("card_number")
        val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
        val AUTH_EMAIL = stringPreferencesKey("auth_email")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            hasCompletedOnboarding = prefs[Keys.HAS_COMPLETED_ONBOARDING] ?: false,
            displayName = prefs[Keys.DISPLAY_NAME] ?: "JJ",
            handle = prefs[Keys.HANDLE] ?: "@focus_champ",
            cardNumber = prefs[Keys.CARD_NUMBER] ?: "0116931",
            isAuthenticated = prefs[Keys.IS_AUTHENTICATED] ?: false,
            authEmail = prefs[Keys.AUTH_EMAIL],
            isProSubscriber = false // Real billing not configured; never fake Pro status
        )
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    suspend fun saveLocalProfile(displayName: String, email: String?) {
        val cleanName = displayName.trim().ifEmpty { "JJ" }
        val cleanHandle = "@" + cleanName.lowercase().replace(Regex("[^a-z0-9_]"), "_").ifEmpty { "focus_champ" }
        context.dataStore.edit { prefs ->
            prefs[Keys.DISPLAY_NAME] = cleanName
            prefs[Keys.HANDLE] = cleanHandle
            prefs[Keys.IS_AUTHENTICATED] = !email.isNullOrBlank()
            if (!email.isNullOrBlank()) {
                prefs[Keys.AUTH_EMAIL] = email.trim()
            }
        }
    }

    suspend fun clearAllUserData() {
        context.dataStore.edit { it.clear() }
    }
}
