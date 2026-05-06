package com.example.projectobcane.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.database.events.Event

import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.ReportsScreenViewModel
import com.example.projectobcane.ui.theme.GreenDark
import com.example.projectobcane.ui.theme.GreenLight
import com.example.projectobcane.ui.theme.RedDark
import com.example.projectobcane.ui.theme.RedLight
import com.example.projectobcane.ui.theme.YellowDark
import com.example.projectobcane.ui.theme.YellowLight
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter




@Composable
fun EventsScreen(
    bottomNav: INavigationRouter,
    rootNav: INavigationRouter,
    paddingValues: PaddingValues
) {

    val viewModel = hiltViewModel<EventsScreenViewModel>()
    val state = viewModel.eventsScreenUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {

        state.value.events.forEach {
            item {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)

                ) {

                    EventItem(
                        event = it
                    ) {
                        rootNav.navigateToEventDetail(it.event.id)
                    }
                }

            }
        }

    }


}








@Composable
fun EventItem(
    event: EventWithWeather,
    onClick: () -> Unit
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.surface
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = halfMargin)
            .clickable { onClick() },
        shape = RoundedCornerShape(basicMargin),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(gradientColors)
                )
                .padding(basicMargin)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Column(
                    modifier = Modifier.weight(1f)
                ) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(halfMargin)
                                )
                                .padding(horizontal = basicMargin, vertical = halfMargin)
                        ) {
                            Text(
                                text = formatEventDate(event.event.date!!),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(halfMargin))


                    Text(
                        text = event.event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(halfMargin))


                    Text(
                        text = "${formatEventTime(event.event.date!!)}, ${event.event.placeName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



fun formatEventDate(timestamp: Long): String {
    if (timestamp == 0L) return "Dnes"
    val formatter = DateTimeFormatter.ofPattern("d. M.")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

fun formatEventTime(timestamp: Long): String {
    if (timestamp == 0L) return "--:--"
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}
