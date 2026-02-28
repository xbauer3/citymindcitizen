package com.example.projectobcane.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.uiDataStore by preferencesDataStore(name = "ui_prefs")

object ThemePreferences {
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    fun isDarkFlow(context: Context): Flow<Boolean> {
        return context.uiDataStore.data.map { prefs ->
            prefs[DARK_MODE_KEY] ?: false
        }
    }

    suspend fun setDark(context: Context, enabled: Boolean) {
        context.uiDataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }
}