package com.example.projectobcane.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.utils.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

            // načteme dark mode jednorázově (flow se sbírá v MainActivity)
            val dark = ThemePreferences.isDarkFlow(context)

            // vezmeme aktuální hodnotu z flow (jednoduše přes map + first by šlo taky)
            // aby to bylo bez dalších importů, uděláme krátký collect v coroutine:
            var darkValue = false
            dark.collect { v ->
                darkValue = v
                return@collect
            }

            _settingsScreenUIState.value = SettingsUIState(
                loading = false,
                selectedLanguage = lang,
                darkModeEnabled = darkValue
            )
        }
    }

    fun updateLanguage(locale: Locale, onChanged: () -> Unit) {
        viewModelScope.launch {
            LanguagePreferences.saveLanguage(context, locale.language)
            LanguageHolder.language = locale.language
            onChanged() // activity.recreate()
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            ThemePreferences.setDark(context, enabled)
            _settingsScreenUIState.value = _settingsScreenUIState.value.copy(darkModeEnabled = enabled)
        }
    }
}