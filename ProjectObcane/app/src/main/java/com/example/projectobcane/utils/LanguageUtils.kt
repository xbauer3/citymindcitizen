package com.example.projectobcane.utils

import java.util.Locale

object LanguageUtils {

    private val CZECH = "cs"


    fun isLanguageCzech(): Boolean{
        val language = Locale.getDefault().language
        return language.equals(CZECH)
    }
}