package com.example.projectobcane.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.utils.CityPreferences
import com.example.projectobcane.utils.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _settingsScreenUIState = MutableStateFlow(SettingsUIState())
    val settingsScreenUIState = _settingsScreenUIState.asStateFlow()

    fun loadSettings() {
        viewModelScope.launch {
            val lang = LanguagePreferences.getSavedLanguage(context)
            val darkEnabled = ThemePreferences.isDarkFlow(context).first()
            val country = CityPreferences.countryFlow(context).first()
            val city = CityPreferences.cityFlow(context).first()

            _settingsScreenUIState.value = SettingsUIState(
                loading = false,
                selectedLanguage = lang,
                darkModeEnabled = darkEnabled,
                selectedCountry = country,
                selectedCity = city
            )
        }
    }

    fun updateLanguage(locale: Locale, onChanged: () -> Unit) {
        viewModelScope.launch {
            LanguagePreferences.saveLanguage(context, locale.language)
            LanguageHolder.language = locale.language
            onChanged()
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            ThemePreferences.setDark(context, enabled)
            _settingsScreenUIState.value = _settingsScreenUIState.value.copy(darkModeEnabled = enabled)
        }
    }

    fun saveLocation(country: String, city: String) {
        viewModelScope.launch {
            CityPreferences.save(context, country, city)
            _settingsScreenUIState.value = _settingsScreenUIState.value.copy(
                selectedCountry = country,
                selectedCity = city
            )
        }
    }
}