package com.example.projectobcane

import android.app.Application
import android.content.Context
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.utils.LocalizedContextWrapper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltAndroidApp
class ProjectObcaneApplication: Application() {




    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageHolder.language
        val wrappedContext = LocalizedContextWrapper.wrap(newBase, lang)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            val savedLang = LanguagePreferences.getSavedLanguage(applicationContext)
            LanguageHolder.language = savedLang
        }




    }


}