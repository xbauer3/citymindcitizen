package com.example.projectobcane.screens.events


import com.example.projectobcane.database.events.Event

data class EventWithWeather(
    val event: Event,
    val weatherEmoji: String? = null
)
