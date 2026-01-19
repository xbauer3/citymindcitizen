package com.example.projectobcane.screens.events.addEdit


import android.location.Location
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report


data class AddEditEventUIState (

    var event: Event =Event(
        id = null,
        title = "",
        description = "",
        category = "",
        status = "",
        placeName = "",
        date = System.currentTimeMillis(),               // means "not set yet"
        location = LocationEntity(0.0,0.0),
        photoUri = "",
        createdAt = System.currentTimeMillis()
    )
    ,

    var loading: Boolean = true,
    var eventSaved: Boolean = false,
    var eventDeleted: Boolean = false,


    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val categoryError: Int? = null,
    val statusError: Int? = null,

    val placeNameError: Int? = null,
    val dateError: Int? = null,


    //val photoError: Int? = null,


)