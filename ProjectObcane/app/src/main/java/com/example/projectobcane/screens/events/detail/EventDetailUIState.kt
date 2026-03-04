package com.example.projectobcane.screens.events.detail

import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.reports.LocationEntity



data class EventDetailUIState (

    var event: Event = Event(
        id = null,
        title = "",
        description = "",
        category = EventCategory.CULTURE.name,
        status = EventStatus.UPCOMING.name,
        placeName = "",
        date = System.currentTimeMillis(),
        location = LocationEntity(0.0,0.0),
        photoUri = "",
        createdAt = System.currentTimeMillis()
    ),
    var loading: Boolean = true,
    var eventDeleted: Boolean = false

)