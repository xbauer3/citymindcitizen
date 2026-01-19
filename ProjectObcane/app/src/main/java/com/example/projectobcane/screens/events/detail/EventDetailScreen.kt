package com.example.projectobcane.screens.events.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.events.EventsScreenViewModel
import com.example.projectobcane.screens.events.formatEventTime
import com.example.projectobcane.screens.reports.detail.ReportDetailUIState
import com.example.projectobcane.screens.reports.detail.ReportDetailViewModel
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.GlideImage
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navigation: INavigationRouter,
    id: Long?
){

    val viewModel = hiltViewModel<EventDetailViewModel>()
    val state = viewModel.eventDetailUIState.collectAsStateWithLifecycle()



    LaunchedEffect(id) {
        viewModel.loadEvent(id)
    }





    BaseScreen(

        topBarText = stringResource(R.string.event_detail),
        showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if (id != null) {



                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = stringResource(R.string.delete_event)) },
                        text = { Text(text = stringResource(R.string.confirm_delete)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteEvent()
                                showDialog = false
                                navigation.returnBack()
                            }) {
                                Text(stringResource(android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(stringResource(android.R.string.cancel))
                            }
                        }
                    )
                }

                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }



            }
        }
    ) {
        EventDetailScreenContent(
            paddingValues = it,
            data = state.value,
            navigation = navigation,


        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreenContent(
    paddingValues: PaddingValues,
    data: EventDetailUIState,
    navigation: INavigationRouter,

){





    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(basicMargin)
    ) {
    // IMAGE
    item {
        GlideImage(
            url = if (data.event.photoUri.isNotEmpty()) data.event.photoUri
            else "https://picsum.photos/400?random=${data.event.id ?: 0}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(basicMargin))
        )
    }

    // TITLE & DESCRIPTION
    item {
        Column {
            Text(
                data.event.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(halfMargin))
            if (data.event.description.isNotEmpty()) {
                Text(data.event.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    // DATE & TIME
    item {
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(basicMargin)) {
                Text("Date & Time", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(halfMargin))
                Text(
                    "Event Date: ${data.event.date?.let { DateUtils.getDateString(it) } ?: "Not set"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Event Time: ${data.event.date?.let { formatEventTime(it) } ?: "--:--"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Created At: ${DateUtils.getDateString(data.event.createdAt)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // CATEGORY & STATUS
    item {
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(basicMargin)) {
                Text("Category & Status", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(halfMargin))
                Text("Category: ${data.event.category}", style = MaterialTheme.typography.bodyMedium)
                Text("Status: ${data.event.status}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    // PLACE & LOCATION
    item {
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(basicMargin)) {
                Text("Place & Location", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(halfMargin))
                Text("Place Name: ${data.event.placeName}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Latitude: ${data.event.location.latitude}, Longitude: ${data.event.location.longitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // ACTION BUTTONS
    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(halfMargin)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    navigation.navigateToAddEditEvent(id = data.event.id)
                }
            ) {
                Text(stringResource(R.string.edit))
            }


        }
    }
}
}