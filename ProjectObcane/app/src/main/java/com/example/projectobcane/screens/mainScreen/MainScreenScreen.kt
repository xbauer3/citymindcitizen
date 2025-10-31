package com.example.projectobcane.screens.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.projectobcane.R
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.BottomBarScreen
import com.example.projectobcane.screens.BottomNavigationBar
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.CalendarGrid
import com.example.projectobcane.ui.elements.CalendarHeader
import com.example.projectobcane.ui.elements.EventRow

import com.example.projectobcane.ui.elements.PlaceholderScreenContent
import com.example.projectobcane.ui.elements.WeekdayLabels
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.heighOfARow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.max


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScreen(navigation: INavigationRouter){


    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state = viewModel.mainScreenUIState.collectAsStateWithLifecycle()



    val selectedDate = viewModel.selectedDate

    val bottomNavController = rememberNavController()


    val events = state.value.events

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }


        BaseScreen(
        topBarText = stringResource(R.string.events),
        floatingActionButton = {
            FloatingActionButton(onClick = {

                val startDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

                navigation.navigateToAddEditEvent(id = null, defaultStartDate = startDateMillis)
                viewModel.refreshEvents()
                //viewModel.loadEvents()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {
                navigation.navigateToSettingsScreen()
            }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
            }
        },
            bottomBar ={ BottomNavigationBar(navController = navigation) }


    ) {
        MainScreenScreenContent(
            paddingValues = it,
            events = events,
            navigation = navigation,
            selectedDate = selectedDate,
            onDateChange = { viewModel.selectedDate = it }
            //actions = viewModel
        )
    }

}
@Composable
fun MainScreenScreenContent(
    paddingValues: PaddingValues,
    events: List<EventWithColorCategory>,
    navigation: INavigationRouter,
    selectedDate : LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(selectedDate.withDayOfMonth(1)) }

    // Sync calendar view when selectedDate changes (e.g., from ViewModel)
    LaunchedEffect(selectedDate) {
        currentMonth = selectedDate.withDayOfMonth(1)
    }

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.dayOfWeek.value % 7

    val days = buildList {
        repeat(firstDayOfWeek) { add(null) }
        for (day in 1..daysInMonth) {
            add(currentMonth.withDayOfMonth(day))
        }
    }

    val eventsForSelectedDay = remember(events, selectedDate) {
        events.filter {
            it.event.startDate?.let { millis ->
                val date = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                date == selectedDate
            } ?: false
        }
    }

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize()
    ) {


        if (eventsForSelectedDay.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.no_events_for_this_day),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {

            eventsForSelectedDay.forEach {
                item {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .padding(bottom = 2.dp)

                    ) {
                        EventRow(
                            event = it
                        ) {
                            navigation.navigateToEventDetail(it.event.id)
                        }
                    }

                }
            }

        }

    }




}


