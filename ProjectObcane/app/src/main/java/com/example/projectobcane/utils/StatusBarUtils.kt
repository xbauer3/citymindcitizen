package com.example.projectobcane.utils

import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat

fun forceWhiteStatusBarIcons(view: View) {
    var parent = view.parent
    while (parent != null && parent !is Window) {
        parent = (parent as? View)?.parent
    }

    val window: Window? = when {
        view.context is android.app.Activity -> (view.context as android.app.Activity).window
        else -> null
    }
    window?.let {
        WindowInsetsControllerCompat(it, it.decorView).isAppearanceLightStatusBars = false
    }
}