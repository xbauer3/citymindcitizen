package com.example.projectobcane.screens.reports.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.reports.IReportLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportDetailViewModel @Inject constructor (private val repository: IReportLocalRepository) : ViewModel() {


    private val _eventDetailUIState: MutableStateFlow<ReportDetailUIState> =
        MutableStateFlow(value = ReportDetailUIState())

    val eventDetailUIState = _eventDetailUIState.asStateFlow()





    fun loadEvent(id: Long?) {
        if (id != null) {
            viewModelScope.launch {

                val reportWithImages = repository.getReportWithImages(id)

                reportWithImages?.let {

                    _eventDetailUIState.value =
                        _eventDetailUIState.value.copy(
                            report = it.report,
                            images = it.images.map { image -> image.imageUri },
                            loading = false
                        )
                }
            }
        }

    }



    fun deleteEvent() {
        viewModelScope.launch {
            repository.deleteReport(_eventDetailUIState.value.report)

            _eventDetailUIState.value = _eventDetailUIState.value.copy(eventDeleted = true)

        }
    }



}