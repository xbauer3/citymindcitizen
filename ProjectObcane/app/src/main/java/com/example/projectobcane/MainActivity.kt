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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            val isDark by ThemePreferences.isDarkFlow(this).collectAsState(initial = false)

            ProjectObcaneTheme(
                darkTheme = isDark,
                dynamicColor = false
            ) {
                NavGraph(startDestination = Destination.SplashScreen.route)
            }
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }
}