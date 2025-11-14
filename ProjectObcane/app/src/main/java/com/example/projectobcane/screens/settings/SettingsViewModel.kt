package com.example.projectobcane.screens.settings

import android.app.Application
import android.content.Context

import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            _settingsScreenUIState.value = SettingsUIState(
                loading = false,
                selectedLanguage = lang
            )
        }
    }

    fun updateLanguage(locale: Locale, onChanged: () -> Unit) {
        viewModelScope.launch {
            LanguagePreferences.saveLanguage(context, locale.language)
            LanguageHolder.language = locale.language
            onChanged() // triggers recreate()
        }
    }

}