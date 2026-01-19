package com.example.projectobcane.screens.events.addEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.R
import com.example.projectobcane.database.events.Event
import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.events.IEventLocalRepository
import com.example.projectobcane.database.reports.IReportLocalRepository
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.Report
import com.example.projectobcane.database.reports.ReportStatus
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenActions
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isBlank
import kotlin.text.isEmpty

@HiltViewModel
class AddEditEventViewModel @Inject constructor (private val repository: IEventLocalRepository) : ViewModel(), AddEditEventActions {

    private var currentId: Long? = null


    private val _addEditEventUIState: MutableStateFlow<AddEditEventUIState> =
        MutableStateFlow(value = AddEditEventUIState())

    val addEditEventUIState = _addEditEventUIState.asStateFlow()


    fun loadEvent(id: Long?) {

        // 🔒 already loaded → DO NOTHING
        if (!_addEditEventUIState.value.loading) return

        if (id != null) {
            viewModelScope.launch {
                val event = repository.getById(id)
                _addEditEventUIState.value = _addEditEventUIState.value.copy(
                    event = event,
                    loading = false
                )
            }
        } else {
            _addEditEventUIState.value = _addEditEventUIState.value.copy(
                loading = false,
                event = Event(
                    id = null,
                    title = "",
                    description = "",
                    category = "",
                    status = "",
                    placeName = "",
                    date = System.currentTimeMillis(),
                    location = LocationEntity(0.0,0.0),
                    photoUri = "",
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }



    override fun onTitleChanged(value: String) {


        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(title = value),
            titleError = error
        )


    }


    override fun onLocationChanged(value: LocationEntity) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(location = value)
        )
    }



    override fun onDescriptionChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(description = value),
            descriptionError = error
        )
    }

    override fun onCategoryChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(category = value),
            categoryError = error
        )
    }

    override fun onStatusChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(status = value),
            statusError = error
        )
    }

    override fun onPlaceNameChanged(value: String) {
        val error = when {
            value.isBlank() -> R.string.error_event_name_required
            value.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(placeName = value),
            placeNameError = error
        )
    }

    override fun onDateChanged(date: Long?) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(date = date), dateError = if (date == null) R.string.can_t_be_empty else null,
            //startDateError = null
        )
    }


    override fun saveEvent() {
        //title
        if (_addEditEventUIState.value.event.title.isEmpty()) {
            _addEditEventUIState.value = _addEditEventUIState.value.copy(titleError = R.string.error_event_name_required)
        }
        else if (_addEditEventUIState.value.event.title.length > 100){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(titleError = R.string.error_event_name_too_long)

        }

        //description
        else if (_addEditEventUIState.value.event.description.isEmpty()){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(descriptionError = R.string.error_event_description_required)
        }
        else if (_addEditEventUIState.value.event.description.length > 250){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(descriptionError = R.string.error_event_description_too_long)
        }

        //category
        else if (_addEditEventUIState.value.event.category.isEmpty()){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(categoryError = R.string.error_event_description_required)
        }
        else if (_addEditEventUIState.value.event.category.length > 250){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(categoryError = R.string.error_event_description_too_long)
        }

        //status
        else if (_addEditEventUIState.value.event.status.isEmpty()){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(statusError = R.string.error_event_description_required)
        }
        else if (_addEditEventUIState.value.event.status.length > 250){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(statusError = R.string.error_event_description_too_long)
        }


        //placeName
        else if (_addEditEventUIState.value.event.placeName.isEmpty()){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(placeNameError = R.string.error_event_description_required)
        }
        else if (_addEditEventUIState.value.event.category.length > 250){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(placeNameError = R.string.error_event_description_too_long)
        }

        //date
        else if (_addEditEventUIState.value.event.date == null){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(dateError = R.string.can_t_be_empty)
        }




        else {


            viewModelScope.launch {
                if (_addEditEventUIState.value.event.id != null) {
                    // update
                    repository.update(_addEditEventUIState.value.event)
                } else {
                    // insert
                    repository.insert(_addEditEventUIState.value.event)
                }
                _addEditEventUIState.value = _addEditEventUIState.value.copy(eventSaved = true)
            }

        }
    }




    override fun deleteEvent() {
        viewModelScope.launch {
            repository.delete(_addEditEventUIState.value.event)
            _addEditEventUIState.value = _addEditEventUIState.value.copy(eventDeleted = true)
        }
    }




}