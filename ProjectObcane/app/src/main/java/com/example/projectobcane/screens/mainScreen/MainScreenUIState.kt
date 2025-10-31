package com.example.projectobcane.screens.mainScreen

import com.example.projectobcane.database.Event
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.database.LocationEntity
import com.example.projectobcane.database.color.ColorCategory

/*
sealed class MainScreenUIState {
    class Default : MainScreenUIState()
    class Success(val events: List<EventWithColorCategory>) : MainScreenUIState()
}
*/



data class MainScreenUIState (

    var events: List<EventWithColorCategory> = emptyList(),
    var loading: Boolean = true,

)