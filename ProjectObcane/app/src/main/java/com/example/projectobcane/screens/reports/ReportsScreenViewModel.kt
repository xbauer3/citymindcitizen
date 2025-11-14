package com.example.projectobcane.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ReportsScreenViewModel  @Inject constructor(private val repository: IReportLocalRepository) : ViewModel() {

    private val _reportsScreenUIState: MutableStateFlow<ReportsUIState> = MutableStateFlow(value = ReportsUIState())



    val reportScreenUIState = _reportsScreenUIState.asStateFlow()


    fun loadReports() {
        viewModelScope.launch {
            repository.getAllReports().collect { reports ->
                _reportsScreenUIState.value = _reportsScreenUIState.value.copy(
                    reports = reports,
                    loading = false
                )
            }
        }
    }




}

