package com.example.submissionawal.data.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class settingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")
    private val reminderKey = booleanPreferencesKey("reminder_setting")


    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }


    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }


    fun getReminderSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[reminderKey] ?: false
        }
    }


    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[reminderKey] = isReminderActive
        }
    }


    companion object {
        @Volatile
        private var INSTACE: settingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): settingsPreferences {
            return INSTACE ?: synchronized(this) {
                val instance = settingsPreferences(dataStore)
                INSTACE = instance
                instance
            }
        }
    }
}