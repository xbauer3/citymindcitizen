package com.example.projectobcane.screens.events.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.screens.reports.detail.ReportDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class EventDetailViewModel @Inject constructor (private val repository: IEventLocalRepository) : ViewModel() {


    private val _eventDetailUIState: MutableStateFlow<EventDetailUIState> =
        MutableStateFlow(value = EventDetailUIState())

    val eventDetailUIState = _eventDetailUIState.asStateFlow()





    fun loadEvent(id: Long?) {
        if (id != null) {
            viewModelScope.launch {
                val event = repository.getById(id)
                _eventDetailUIState.value = _eventDetailUIState.value.copy(
                    event = event,

                    loading = false
                )
            }
        }

    }



    fun deleteEvent() {
        viewModelScope.launch {
            repository.delete(_eventDetailUIState.value.event)

            _eventDetailUIState.value = _eventDetailUIState.value.copy(eventDeleted = true)

        }
    }



}