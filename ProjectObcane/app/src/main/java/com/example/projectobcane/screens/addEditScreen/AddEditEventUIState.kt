package com.example.projectobcane.screens.addEditScreen

import com.example.projectobcane.database.Event
import com.example.projectobcane.database.LocationEntity
import com.example.projectobcane.navigation.EventLocation

import java.time.LocalDateTime

data class AddEditEventUIState (
    var event: Event = Event(null, "", "", null, null, null, null, LocationEntity(0.0, 0.0)),

    val eventLocations: List<EventLocation> = emptyList(),

    val eventNameError: Int? = null,
    val eventDescriptionError: Int? = null,
    val colorError: Int? = null,


    val startDateError: Int? = null,
    val endDateError: Int? = null,

    val notificationDateError: Int? = null,
    /*
    val locationError: Int? = null,*/

    val initialized: Boolean = false,

    var loading: Boolean = true,
    var eventSaved: Boolean = false,
    var eventDeleted: Boolean = false
)