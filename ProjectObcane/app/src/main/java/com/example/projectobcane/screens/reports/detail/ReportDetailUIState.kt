package com.example.projectobcane.screens.reports.detail

import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.ReportEntity
import com.example.projectobcane.database.reports.ReportStatus


data class ReportDetailUIState (

    var report: ReportEntity = ReportEntity(
        localId = 0,
        remoteId = null,
        title = "",
        description = "",
        reportType = "",
        status = ReportStatus.NEW.value,
        location = LocationEntity(0.0, 0.0),
        entityId = 1942,
        hashedEmail = null,
        dateAdded = null,
        createdAt = System.currentTimeMillis()
    ),

    var images: List<String> = emptyList(),

    var loading: Boolean = true,
    var eventDeleted: Boolean = false
)