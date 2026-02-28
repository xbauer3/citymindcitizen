package com.example.projectobcane

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.projectobcane.navigation.Destination
import com.example.projectobcane.navigation.NavGraph
import android.Manifest
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.utils.LocalizedContextWrapper
import com.example.projectobcane.ui.theme.ProjectObcaneTheme
import com.example.projectobcane.utils.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun attachBaseContext(base: Context) {
        val savedLang = runBlocking { LanguagePreferences.getSavedLanguage(base) }
        LanguageHolder.language = savedLang
        val wrappedContext = LocalizedContextWrapper.wrap(base, savedLang)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()

        setContent {
            val isDark by ThemePreferences.isDarkFlow(this).collectAsState(initial = false)

            ProjectObcaneTheme(
                darkTheme = isDark,
                dynamicColor = false // necháváme konzistentní barvy
            ) {
                NavGraph(startDestination = Destination.SplashScreen.route)
            }
        }
    }
}