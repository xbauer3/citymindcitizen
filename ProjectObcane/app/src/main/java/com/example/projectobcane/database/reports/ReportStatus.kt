package com.example.projectobcane.database.reports

enum class ReportStatus(val value: String, val label: String) {
    NEW("new", "New"),
    IN_PROGRESS("inprogress", "In Progress"),
    COMPLETE("complete", "Complete")
}
