package com.example.projectobcane.screens.reports.detail

import com.example.projectobcane.database.reports.Report


data class ReportDetailUIState (

    var report: Report = Report(null, "", "", "", "", 0.0, 0.0,"", 0L),
    var loading: Boolean = true,
    var eventDeleted: Boolean = false

)