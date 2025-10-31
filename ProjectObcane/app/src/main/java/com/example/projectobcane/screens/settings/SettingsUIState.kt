package com.example.projectobcane.screens.settings

import java.util.Locale


data class SettingsUIState(
    val loading: Boolean = true,
    val selectedLanguage: String = "en",
    val supportedLanguages: List<Locale> = listOf(Locale("en"), Locale("cs"))
)