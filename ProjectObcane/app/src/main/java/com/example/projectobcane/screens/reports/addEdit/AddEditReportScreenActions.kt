package com.example.projectobcane.screens.reports.addEdit

import android.content.Context
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report


interface AddEditReportScreenActions {

    fun onTitleChanged(value: String)
    fun onDescriptionChanged(value: String)
    fun onCategoryChanged(value: String)
    fun onStatusChanged(value: String)

    fun onPhotoSelected(
        context: Context,
        uri: String
    )

    fun onLocationChanged(value: LocationEntity)

    fun saveReport()
    fun deleteReport()
}
