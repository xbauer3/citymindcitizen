package com.example.projectobcane.screens.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IWeatherRemoteRepository
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.screens.reports.ReportsUIState
import com.example.projectobcane.utils.mapWeatherCodeToEmoji
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
@HiltViewModel
class EventsScreenViewModel @Inject constructor(
    private val eventRepository: IEventLocalRepository,
    private val weatherRepository: IWeatherRemoteRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(EventsScreenUIState())

    val eventsScreenUIState = _uiState.asStateFlow()

    init {
        loadEvents()
    }


    fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getAll().collect { events ->

                Log.d("EVENTS_DEBUG", "All events count = ${events.size}")

                events.forEach {
                    Log.d(
                        "EVENTS_DEBUG",
                        "Event id=${it.id}, date=${it.date}, status=${it.status}"
                    )
                }


                val upcomingEvents = events
                    //.filter { it.date != null && it.date!! > System.currentTimeMillis() }
                    //.filter { it.status != "CANCELLED" && it.status != "COMPLETED" }

                // 1️⃣ zobrazíme hned bez počasí
                _uiState.value = EventsScreenUIState(
                    loading = false,
                    events = upcomingEvents.map {
                        EventWithWeather(it)
                    }
                )

                // 2️⃣ počasí dopočítáme ASYNC
                upcomingEvents.forEach { event ->
                    loadWeatherAsync(event)
                }
            }
        }
    }

    private fun loadWeatherAsync(event: Event) {
        viewModelScope.launch {
            val lat = event.location.latitude
            val lng = event.location.longitude
            val dateMillis = event.date

            if (lat == null || lng == null || dateMillis == null) return@launch

            val date = Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ISO_DATE)

            val result = weatherRepository.getWeatherCode(lat, lng, date)

            when (result) {
                is CommunicationResult.Success -> {
                    Log.d(
                        "WEATHER_API",
                        "Event ${event.id} → weatherCode=${result.data}"
                    )

                    updateWeather(event.id!!, mapWeatherCodeToEmoji(result.data))
                }

                is CommunicationResult.Error -> {
                    Log.d("WEATHER_API_ERROR", result.error.message ?: "error")
                }

                is CommunicationResult.ConnectionError -> {
                    Log.d("WEATHER_API", "No internet")
                }

                is CommunicationResult.Exception -> {
                    Log.d("WEATHER_API", result.exception.message ?: "exception")
                }
            }
        }
    }

    private fun updateWeather(eventId: Long, emoji: String) {
        _uiState.value = _uiState.value.copy(
            events = _uiState.value.events.map {
                if (it.event.id == eventId)
                    it.copy(weatherEmoji = emoji)
                else it
            }
        )
    }
}
