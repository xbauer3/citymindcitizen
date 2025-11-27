package com.example.projectobcane.screens.notifications

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.projectobcane.R
import com.example.projectobcane.di.notifications.NotificationBuilderFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NotificationsScreenViewModel @Inject constructor (
    //private val repository: IEventLocalRepository
    private val builderFactory: NotificationBuilderFactory,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {

    @SuppressLint("MissingPermission")
    fun sendNotification(channelId: String, title: String, message: String) {
        val id = (0..999999).random()
        val notification = builderFactory.build(channelId, title, message).build()
        notificationManager.notify(id, notification)
    }

    fun sendEventsNotification() {
        sendNotification("events", "New Event", "A new event has been added.")
    }

    fun sendImportantAlert() {
        sendNotification("important_alerts", "URGENT ALERT", "Water outage tomorrow!")
    }

    fun sendReportNotification() {
        sendNotification("reports", "Report Update", "Your report has changed status.")
    }

    fun sendMapNotification() {
        sendNotification("map_updates", "Map Update", "A new location was added.")
    }

    fun sendVotingNotification() {
        sendNotification("voting", "New Voting", "Vote for the new park changes.")
    }

    fun sendAdminNotification() {
        sendNotification("admin_tools", "New Report", "A new report has been submitted.")
    }


}