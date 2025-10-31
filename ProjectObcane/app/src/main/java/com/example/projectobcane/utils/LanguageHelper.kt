package com.example.projectobcane.screens.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageManager {

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale) // affects date formatting!

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        val updatedContext = context.createConfigurationContext(config)

        // Apply to application resources as well
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        return updatedContext
    }
}




private val Context.dataStore by preferencesDataStore(name = "language_prefs")

object LanguagePreferences {
    private val LANGUAGE_KEY = stringPreferencesKey("language")

    suspend fun saveLanguage(context: Context, langCode: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = langCode
        }
    }

    suspend fun getSavedLanguage(context: Context): String {
        return context.dataStore.data
            .map { it[LANGUAGE_KEY] ?: "en" }
            .first()
    }
}



object LanguageHolder {
    var language: String = Locale.getDefault().language
}