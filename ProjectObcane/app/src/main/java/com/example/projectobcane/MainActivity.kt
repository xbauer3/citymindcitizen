package com.example.projectobcane

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.projectobcane.navigation.Destination
import com.example.projectobcane.navigation.NavGraph


import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.utils.LocalizedContextWrapper
import com.example.projectobcane.ui.theme.ProjectObcaneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(base: Context) {
        val savedLang = runBlocking { LanguagePreferences.getSavedLanguage(base) }
        LanguageHolder.language = savedLang
        val wrappedContext = LocalizedContextWrapper.wrap(base, savedLang)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectObcaneTheme {
                NavGraph(startDestination = Destination.SplashScreen.route)
            }
        }
    }
}