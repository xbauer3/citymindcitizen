package com.example.projectobcane.screens.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.IEventLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject



@HiltViewModel
class EventsScreenViewModel  @Inject constructor(private val repository: IEventLocalRepository) : ViewModel() {

    private val _eventsScreenUIState: MutableStateFlow<EventsScreenUIState> =
        MutableStateFlow(value = EventsScreenUIState())


    var selectedDate by mutableStateOf(LocalDate.now())



    val mainScreenUIState = _eventsScreenUIState.asStateFlow()

/*
    fun loadEvents(){
        viewModelScope.launch {
            repository.getAll().collect { events ->
                _mainScreenUIState.update {
                    MainScreenUIState.Success(events)
                }
            }
        }



    }

*/
fun loadEvents() {
    viewModelScope.launch {
        val events = repository.getAll().collect() { events ->
            _eventsScreenUIState.value = _eventsScreenUIState.value.copy(
                events = events,
                loading = false
            )
        }

    }
}



    fun refreshEvents() {
        viewModelScope.launch {
            repository.getAll().collect { events ->
                val filtered = events.filter { it.event.startDate != null }
                //_mainScreenUIState.value = MainScreenUIState.Success(filtered)
                _eventsScreenUIState.value = _eventsScreenUIState.value.copy(events = filtered,loading = false)
            }
        }
    }



}