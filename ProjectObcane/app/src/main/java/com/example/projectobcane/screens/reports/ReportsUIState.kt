package com.example.projectobcane.screens.reports


import com.example.projectobcane.database.reports.ReportEntity
import com.example.projectobcane.database.reports.ReportWithImages


data class ReportsUIState (
    var reports: List<ReportWithImages> = emptyList(),
    var loading: Boolean = true,
)