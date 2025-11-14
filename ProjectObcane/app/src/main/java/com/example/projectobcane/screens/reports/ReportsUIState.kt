package com.example.projectobcane.screens.reports

import com.example.projectobcane.database.reports.Report


data class ReportsUIState (
    var reports: List<Report> = emptyList(),
    var loading: Boolean = true,
)