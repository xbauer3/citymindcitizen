package com.example.projectobcane

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectobcane.navigation.Destination
import com.example.projectobcane.navigation.NavGraph

import android.Manifest
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.utils.LocalizedContextWrapper
import com.example.projectobcane.ui.theme.ProjectObcaneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Optional: show a toast or log
    }


    override fun attachBaseContext(base: Context) {
        val savedLang = runBlocking { LanguagePreferences.getSavedLanguage(base) }
        LanguageHolder.language = savedLang
        val wrappedContext = LocalizedContextWrapper.wrap(base, savedLang)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Request permission before UI loads
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()
        setContent {
            ProjectObcaneTheme {
                NavGraph(startDestination = Destination.SplashScreen.route)
            }
        }
    }
}