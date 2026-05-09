package com.example.projectobcane.screens.notifications


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter

import com.example.projectobcane.ui.theme.basicMargin


@Composable
fun NotificationsScreen(navigation: INavigationRouter, paddingValues: PaddingValues) {

    val viewModel = hiltViewModel<NotificationsScreenViewModel>()



    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(basicMargin)
            .verticalScroll(rememberScrollState())
    ) {

        Text(stringResource(R.string.test_notifications), style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(basicMargin))

        NotificationTestButton("Send Event Notification") {
            viewModel.sendEventsNotification()
            //sendTestNotification("events", "New Event", "A new event was added.")
        }

        NotificationTestButton("Send Important Alert") {
            viewModel.sendImportantAlert()
            //sendTestNotification("important_alerts", "Important Alert!", "Water outage tomorrow 8:00–14:00.")
        }

        NotificationTestButton("Send Report Update") {
            viewModel.sendReportNotification()
            //sendTestNotification("reports", "Report Updated", "Your pothole report is now in progress.")
        }

        NotificationTestButton("Send Map Update") {
            viewModel.sendMapNotification()
            //sendTestNotification("map_updates", "Map Updated", "A new parking spot has been added.")
        }

        NotificationTestButton("Send Voting Notification") {
            viewModel.sendVotingNotification()
            //sendTestNotification("voting", "New Voting", "Vote for new park improvements!")
        }

        NotificationTestButton("Send Admin Notification") {
            viewModel.sendAdminNotification()
            //sendTestNotification("admin_tools", "New Report", "A citizen submitted a new issue!")
        }

        Spacer(Modifier.height(basicMargin))


    }
}






@Composable
fun NotificationTestButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(text)
    }
}