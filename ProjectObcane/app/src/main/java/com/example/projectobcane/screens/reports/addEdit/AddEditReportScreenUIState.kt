package com.example.projectobcane.screens.reports.addEdit

import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report


data class AddEditReportScreenUIState (

    var report: Report = Report(null, "", "", "", "", LocationEntity(0.0,0.0),"", 0L),

    var loading: Boolean = true,
    var reportSaved: Boolean = false,
    var reportDeleted: Boolean = false,


    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val categoryError: Int? = null,
    val statusError: Int? = null,

    //val photoError: Int? = null,


)