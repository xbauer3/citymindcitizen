package com.example.projectobcane.screens.events

import com.example.projectobcane.database.EventWithColorCategory

/*
sealed class MainScreenUIState {
    class Default : MainScreenUIState()
    class Success(val events: List<EventWithColorCategory>) : MainScreenUIState()
}
*/



data class EventsScreenUIState (

    var events: List<EventWithColorCategory> = emptyList(),
    var loading: Boolean = true,

)