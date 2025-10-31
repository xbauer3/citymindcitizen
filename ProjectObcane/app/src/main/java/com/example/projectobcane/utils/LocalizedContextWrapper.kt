package com.example.projectobcane.utils


import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import android.content.res.Configuration
import java.util.Locale

object LocalizedContextWrapper {

    fun wrap(context: Context, language: String): ContextWrapper {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            config.setLocales(LocaleList(locale))
        } else {
            config.setLocale(locale)
            config.setLayoutDirection(locale)
        }

        val newContext = context.createConfigurationContext(config)
        return ContextWrapper(newContext)
    }
}
