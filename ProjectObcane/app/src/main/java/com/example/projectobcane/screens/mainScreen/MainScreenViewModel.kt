package com.example.projectobcane.screens.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.IEventLocalRepository
import com.example.projectobcane.screens.detailScreen.EventDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject



@HiltViewModel
class MainScreenViewModel  @Inject constructor(private val repository: IEventLocalRepository) : ViewModel() {

    private val _mainScreenUIState: MutableStateFlow<MainScreenUIState> =
        MutableStateFlow(value = MainScreenUIState())


    var selectedDate by mutableStateOf(LocalDate.now())



    val mainScreenUIState = _mainScreenUIState.asStateFlow()

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
            _mainScreenUIState.value = _mainScreenUIState.value.copy(
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
                _mainScreenUIState.value = _mainScreenUIState.value.copy(events = filtered,loading = false)
            }
        }
    }



}