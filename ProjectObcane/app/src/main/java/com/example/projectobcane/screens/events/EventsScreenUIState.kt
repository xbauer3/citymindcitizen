package com.example.projectobcane.screens.events

import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.reports.Report


data class EventsScreenUIState (
    var events: List<Event> = emptyList(),
    var loading: Boolean = true,
)