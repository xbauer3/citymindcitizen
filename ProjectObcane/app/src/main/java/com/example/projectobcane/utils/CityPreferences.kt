package com.example.projectobcane.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cityDataStore by preferencesDataStore(name = "city_prefs")

object CityPreferences {
    private val COUNTRY_KEY = stringPreferencesKey("selected_country")
    private val CITY_KEY = stringPreferencesKey("selected_city")

    fun countryFlow(context: Context): Flow<String?> =
        context.cityDataStore.data.map { it[COUNTRY_KEY] }

    fun cityFlow(context: Context): Flow<String?> =
        context.cityDataStore.data.map { it[CITY_KEY] }

    suspend fun save(context: Context, country: String, city: String) {
        context.cityDataStore.edit { prefs ->
            prefs[COUNTRY_KEY] = country
            prefs[CITY_KEY] = city
        }
    }
}