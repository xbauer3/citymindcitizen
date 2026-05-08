package com.example.projectobcane

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.utils.LocalizedContextWrapper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import com.example.projectobcane.communication.auth.IAuthRemoteRepository
import com.example.projectobcane.communication.auth.TokenManager

import com.example.projectobcane.communication.CommunicationResult

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ProjectObcaneApplication: Application() {

    @Inject
    lateinit var authRepository: IAuthRemoteRepository

    @Inject
    lateinit var tokenManager: TokenManager


    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageHolder.language
        val wrappedContext = LocalizedContextWrapper.wrap(newBase, lang)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()

        CoroutineScope(Dispatchers.Default).launch {
            val savedLang = LanguagePreferences.getSavedLanguage(applicationContext)
            LanguageHolder.language = savedLang
        }

        // Login runs in parallel with the splash animation —
        // by the time the user taps through onboarding or reaches
        // the main screen the token will already be saved.
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("APP", "Auto-login starting...")
            when (val result = authRepository.login(
                BuildConfig.AUTH_USERNAME,
                BuildConfig.AUTH_PASSWORD
            )) {
                is CommunicationResult.Success -> {
                    tokenManager.saveToken(result.data.accessToken)
                    Log.d("APP", "Auto-login SUCCESS")
                }
                is CommunicationResult.Error ->
                    Log.e("APP", "Auto-login ERROR ${result.error.code}: ${result.error.message}")
                is CommunicationResult.ConnectionError ->
                    Log.e("APP", "Auto-login CONNECTION ERROR")
                is CommunicationResult.Exception ->
                    Log.e("APP", "Auto-login EXCEPTION: ${result.exception.message}", result.exception)
            }
        }



    }


    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channels = listOf(
                NotificationChannel("events", "Events", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel("important_alerts", "Important Alerts", NotificationManager.IMPORTANCE_HIGH),
                NotificationChannel("reports", "Reports", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel("map_updates", "Map Updates", NotificationManager.IMPORTANCE_LOW),
                NotificationChannel("voting", "Voting", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel("admin_tools", "Admin Tools", NotificationManager.IMPORTANCE_HIGH),
            )

            val manager = getSystemService(NotificationManager::class.java)
            channels.forEach { manager.createNotificationChannel(it) }
        }
    }


}


