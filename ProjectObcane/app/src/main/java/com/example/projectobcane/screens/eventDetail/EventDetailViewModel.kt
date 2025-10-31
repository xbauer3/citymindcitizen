package com.example.projectobcane.screens.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.Event
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.database.IEventLocalRepository
import com.example.projectobcane.database.LocationEntity
import com.example.projectobcane.screens.addEditScreen.AddEditEventActions
import com.example.projectobcane.screens.addEditScreen.AddEditEventUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
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
                    event = event.event,
                    colorCategory = event.colorCategory,
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