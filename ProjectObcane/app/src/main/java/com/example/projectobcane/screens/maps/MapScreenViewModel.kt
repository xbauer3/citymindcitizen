package com.example.projectobcane.screens.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val reportRepository: IReportLocalRepository,
    private val eventRepository: IEventLocalRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<MapItem>>(emptyList())
    val items = _items.asStateFlow()

    fun loadMapItems() {
        viewModelScope.launch {
            combine(
                reportRepository.getAllReports(),
                eventRepository.getAll()
            ) { reports, events ->

                val reportItems = reports
                    .filter { it.location.latitude != null && it.location.longitude != null }
                    .map {
                        MapItem.ReportItem(
                            reportId = it.id!!,
                            lat = it.location.latitude!!,
                            lng = it.location.longitude!!,
                            reportTitle = it.title
                        )
                    }

                val eventItems = events
                    .filter { it.location.latitude != null && it.location.longitude != null }
                    .map {
                        MapItem.EventItem(
                            eventId = it.id!!,
                            lat = it.location.latitude!!,
                            lng = it.location.longitude!!,
                            eventTitle = it.title
                        )
                    }

                reportItems + eventItems
            }.collect {
                _items.value = it
            }
        }
    }
}
