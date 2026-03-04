package com.example.projectobcane.screens.reports.addEdit

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.R
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.ReportEntity
import com.example.projectobcane.database.reports.ReportImageEntity
import com.example.projectobcane.database.reports.ReportStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import kotlin.text.insert



@HiltViewModel
class AddEditReportScreenViewModel @Inject constructor (private val repository: IReportLocalRepository) : ViewModel(), AddEditReportScreenActions {

    private var currentId: Long? = null




    private val _addEditReportUIState: MutableStateFlow<AddEditReportScreenUIState> =
        MutableStateFlow(value = AddEditReportScreenUIState())

    val addEditReportUIState = _addEditReportUIState.asStateFlow()


    fun loadReport(id: Long?) {
        // 🔒 already loaded → DO NOTHING
        if (!_addEditReportUIState.value.loading) return

        if (id != null) {
            viewModelScope.launch {
                val reportWithImages = repository.getReportWithImages(id)
                _addEditReportUIState.value = _addEditReportUIState.value.copy(
                    report = reportWithImages?.report ?: return@launch,
                    images = reportWithImages.images.map { it.imageUri },
                    loading = false
                )
            }
        } else {
            _addEditReportUIState.value = _addEditReportUIState.value.copy(
                loading = false,
                report = ReportEntity(
                    localId = null,
                    remoteId = null,
                    title = "",
                    description = "",
                    reportType = "",
                    status = ReportStatus.NEW.value,
                    location = LocationEntity(0.0, 0.0),
                    entityId = 1942,
                    hashedEmail = null,
                    dateAdded = null,
                    createdAt = System.currentTimeMillis()
                ),
                images = emptyList()
            )
        }
    }



    override fun onTitleChanged(value: String) {


        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditReportUIState.value = _addEditReportUIState.value.copy(
            report = _addEditReportUIState.value.report.copy(title = value),
            titleError = error
        )


    }


    override fun onLocationChanged(value: LocationEntity) {
        _addEditReportUIState.value = _addEditReportUIState.value.copy(
            report = _addEditReportUIState.value.report.copy(location = value)
        )
    }



    override fun onDescriptionChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditReportUIState.value = _addEditReportUIState.value.copy(
            report = _addEditReportUIState.value.report.copy(description = value),
            descriptionError = error
        )
    }

    override fun onCategoryChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditReportUIState.value = _addEditReportUIState.value.copy(
            report = _addEditReportUIState.value.report.copy(reportType = value),
            categoryError = error
        )
    }

    override fun onStatusChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditReportUIState.value = _addEditReportUIState.value.copy(
            report = _addEditReportUIState.value.report.copy(status = value),
            statusError = error
        )
    }





    override fun onPhotoSelected(
        context: Context,
        uri: String
    ) {
        _addEditReportUIState.update {
            it.copy(
                images = it.images + uri
            )
        }
    }





    override fun saveReport() {
        //title
        if (_addEditReportUIState.value.report.title.isEmpty()) {
            _addEditReportUIState.value = _addEditReportUIState.value.copy(titleError = R.string.error_event_name_required)
        }
        else if (_addEditReportUIState.value.report.title.length > 100){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(titleError = R.string.error_event_name_too_long)

        }

        //description
        else if (_addEditReportUIState.value.report.description.isEmpty()){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(descriptionError = R.string.error_event_description_required)
        }
        else if (_addEditReportUIState.value.report.description.length > 250){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(descriptionError = R.string.error_event_description_too_long)
        }

        //category
        else if (_addEditReportUIState.value.report.reportType.isEmpty()){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(categoryError = R.string.error_event_description_required)
        }
        else if (_addEditReportUIState.value.report.reportType.length > 250){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(categoryError = R.string.error_event_description_too_long)
        }

        //status
        else if (_addEditReportUIState.value.report.status.isEmpty()){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(statusError = R.string.error_event_description_required)
        }
        else if (_addEditReportUIState.value.report.status.length > 250){
            _addEditReportUIState.value = _addEditReportUIState.value.copy(statusError = R.string.error_event_description_too_long)
        }


        else {


            viewModelScope.launch {

                val state = _addEditReportUIState.value
                val report = state.report

                val reportId = if (report.localId != null && report.localId!! > 0) {

                    repository.updateReport(report)
                    report.localId!!

                } else {

                    repository.insertReport(report)
                }

                // 🔥 Delete old images (important when editing)
                repository.deleteImagesForReport(reportId)

                // 🔥 Insert new images
                val imageEntities = state.images.map { uri ->
                    ReportImageEntity(
                        reportLocalId = reportId,
                        imageUri = uri
                    )
                }

                repository.insertImages(imageEntities)

                _addEditReportUIState.value =
                    _addEditReportUIState.value.copy(reportSaved = true)
            }

        }
    }




    override fun deleteReport() {
        viewModelScope.launch {
            repository.deleteReport(_addEditReportUIState.value.report)
            _addEditReportUIState.value = _addEditReportUIState.value.copy(reportDeleted = true)
        }
    }




}

