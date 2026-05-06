package com.example.projectobcane.screens.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.IEventLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsScreenViewModel @Inject constructor(
    private val eventRepository: IEventLocalRepository
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


                _uiState.value = EventsScreenUIState(
                    loading = false,
                    events = upcomingEvents.map {
                        EventWithWeather(it)
                    }
                )
            }
        }
    }
}
