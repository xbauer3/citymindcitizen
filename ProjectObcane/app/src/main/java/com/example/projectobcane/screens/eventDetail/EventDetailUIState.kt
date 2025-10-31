package com.example.projectobcane.screens.detailScreen

import com.example.projectobcane.database.Event
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.database.LocationEntity
import com.example.projectobcane.database.color.ColorCategory


data class EventDetailUIState (

    var event: Event = Event(null, "", "", null, null, null, null, LocationEntity(0.0, 0.0)),
    var colorCategory: ColorCategory? = null,
    var loading: Boolean = true,
    var eventDeleted: Boolean = false
)