package com.example.projectobcane.screens.chLocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.navigation.EventLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChooseLocationViewModel @Inject constructor( private val repository: IReportLocalRepository) : ViewModel(), ChooseLocationActions {

    private val _uiState = MutableStateFlow(ChooseLocationUIState())
    val uiState = _uiState.asStateFlow()



    private val _eventLocations = MutableStateFlow<List<EventLocation>>(emptyList())
    val eventLocations: StateFlow<List<EventLocation>> = _eventLocations

    init {
        viewModelScope.launch {
            _eventLocations.value = listOf<EventLocation>()
        }
    }



    override fun locationChanged(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(
            latitude = latitude,
            longitude = longitude,
            locationChanged = true
        )
    }
}