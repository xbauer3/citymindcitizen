package com.example.projectobcane.screens.events

import com.example.projectobcane.screens.events.EventWithWeather

data class EventsScreenUIState(
    val loading: Boolean = true,
    val events: List<EventWithWeather> = emptyList(),
    val error: Int? = null
)
