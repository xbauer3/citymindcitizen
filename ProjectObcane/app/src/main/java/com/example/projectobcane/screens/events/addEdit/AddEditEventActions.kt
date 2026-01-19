package com.example.projectobcane.screens.events.addEdit

import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.reports.LocationEntity



interface AddEditEventActions {


    fun onTitleChanged(value: String)
    fun onDescriptionChanged(value: String)
    fun onCategoryChanged(value: String)
    fun onStatusChanged(value: String)


    fun onPlaceNameChanged(value: String)
    fun onDateChanged(date: Long?)



    //fun onPhotoChanged(value: String)

    fun onLocationChanged(value: LocationEntity)

    fun saveEvent()
    fun deleteEvent()

}