package com.example.projectobcane.di.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.projectobcane.R

class NotificationBuilderFactory(private val context: Context) {

    fun build(channelId: String, title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }
}
