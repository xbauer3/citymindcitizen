package com.example.projectobcane.screens.addEditScreen

import com.example.projectobcane.database.LocationEntity


interface AddEditEventActions {
    fun onEventNameChanged(value: String)
    fun onEventDescriptionChanged(value: String)
    fun onStartDateChanged(date: Long?)
    fun onEndDateChanged(date: Long?)
    fun onColorChanged(value: Long?)
    fun onNotificationDateChanged(date: Long?)
    fun onLocationChanged(value: LocationEntity)
    fun saveEvent()
    fun deleteEvent()




}