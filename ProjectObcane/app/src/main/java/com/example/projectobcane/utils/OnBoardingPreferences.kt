package com.example.projectobcane.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

object OnboardingPreferences {

    private val COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    /**
     * Marks onboarding as completed so it won't be shown again.
     */
    suspend fun setCompleted(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[COMPLETED_KEY] = true
        }
    }

    /**
     * Returns true if user already completed onboarding.
     */
    suspend fun isCompleted(context: Context): Boolean {
        return context.dataStore.data
            .map { it[COMPLETED_KEY] ?: false }
            .first()
    }


    /**
     * Resets onboarding so i can see changes to the screens.
     */
    suspend fun reset(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[COMPLETED_KEY] = false
        }
    }
}
