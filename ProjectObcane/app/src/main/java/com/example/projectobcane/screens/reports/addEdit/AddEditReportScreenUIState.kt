package com.example.projectobcane.screens.reports.addEdit

import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.ReportEntity
import com.example.projectobcane.database.reports.ReportStatus


data class AddEditReportScreenUIState (

    var report: ReportEntity = ReportEntity(
        localId = null,
        remoteId = null,
        title = "",
        description = "",
        reportType = "",
        status = ReportStatus.NEW.value,
        location = LocationEntity(0.0, 0.0),
        entityId = 1932,
        hashedEmail = null,
        dateAdded = null,
        createdAt = System.currentTimeMillis()
    ),

    var images: List<String> = emptyList(),

    var loading: Boolean = true,
    var reportSaved: Boolean = false,
    var reportDeleted: Boolean = false,

    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val categoryError: Int? = null,
    val statusError: Int? = null,
)