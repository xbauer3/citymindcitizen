package com.example.projectobcane.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.screens.reports.ReportsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsScreenViewModel  @Inject constructor(private val repository: IEventLocalRepository) : ViewModel() {

    private val _eventsScreenUIState: MutableStateFlow<EventsScreenUIState> =
        MutableStateFlow(value = EventsScreenUIState())



    val eventsScreenUIState = _eventsScreenUIState.asStateFlow()


    fun loadEvents() {
        viewModelScope.launch {
            repository.getAll().collect { events ->
                _eventsScreenUIState.value = _eventsScreenUIState.value.copy(
                    events = events,
                    loading = false
                )
            }
        }
    }




}