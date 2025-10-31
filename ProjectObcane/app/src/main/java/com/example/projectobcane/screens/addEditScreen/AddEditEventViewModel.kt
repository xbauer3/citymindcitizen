package com.example.projectobcane.screens.addEditScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.database.Event
import com.example.projectobcane.database.IEventLocalRepository
import com.example.projectobcane.database.LocationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import com.example.projectobcane.R
import kotlin.text.insert


@HiltViewModel
class AddEditEventViewModel @Inject constructor (private val repository: IEventLocalRepository) : ViewModel(), AddEditEventActions {

    private var currentId: Long? = null


    private val _addEditEventUIState: MutableStateFlow<AddEditEventUIState> =
        MutableStateFlow(value = AddEditEventUIState())

    val addEditEventUIState = _addEditEventUIState.asStateFlow()


    fun loadEventLocations() {
        viewModelScope.launch {
            val locations = repository.getEventLocations()
            _addEditEventUIState.update {
                it.copy(eventLocations = locations) // <-- Add this field to your UI state
            }
        }
    }


    fun initialize(id: Long?, defaultStartDate: Long?) {
        if (_addEditEventUIState.value.initialized) return

        currentId = id
        _addEditEventUIState.update {
            it.copy(initialized = true)
        }

        loadEvent(id, defaultStartDate)
    }


    fun loadEvent(id: Long?, defaultStartDate: Long?) {
        if (id != null) {
            viewModelScope.launch {
                val event = repository.getById(id)
                _addEditEventUIState.value = _addEditEventUIState.value.copy(
                    event = event.event,
                    loading = false
                )
            }
        } else {

            val start = defaultStartDate
            val end = defaultStartDate?.let {
                // Add 1 day in millis
                it + Duration.ofDays(1).toMillis()
            }

            _addEditEventUIState.value = _addEditEventUIState.value.copy(
                loading = false,
                event = Event(null, "", "", start, end, null, start, LocationEntity(0.0, 0.0))

            )
        }

    }






    override fun onEventNameChanged(name: String) {


        val error = when {
            name.isBlank() -> R.string.error_event_name_required
            name.length > 100 -> R.string.error_event_name_too_long
            else -> null
        }

        _addEditEventUIState.value = _addEditEventUIState.value.copy(event = _addEditEventUIState.value.event.copy(eventName = name),eventNameError = error)


        //_addEditEventUIState.value = _addEditEventUIState.value.copy(event = _addEditEventUIState.value.event.copy(eventName = name),eventNameError = if (name.isNotBlank()) null else _addEditEventUIState.value.eventNameError)

    }

    override fun onEventDescriptionChanged(des: String) {


        val error = when {
            des.isBlank() -> R.string.error_event_description_required
            des.length > 250 -> R.string.error_event_description_too_long
            else -> null
        }


        _addEditEventUIState.value = _addEditEventUIState.value.copy(event = _addEditEventUIState.value.event.copy(eventDescription = des), eventDescriptionError = error)

        //_addEditEventUIState.value = _addEditEventUIState.value.copy(event = _addEditEventUIState.value.event.copy(eventDescription = des),eventNameError = if (des.isNotBlank()) null else _addEditEventUIState.value.eventDescriptionError)
    }


    override fun onStartDateChanged(date: Long?) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(startDate = date), startDateError = if (date == null) R.string.can_t_be_empty else null,
            //startDateError = null
        )
    }

    override fun onEndDateChanged(date: Long?) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(endDate = date), endDateError = if (date == null) R.string.can_t_be_empty else null,
            //startDateError = null
        )
    }

    override fun onColorChanged(col: Long?) {


        if (_addEditEventUIState.value.event.colorCategoryId == null) R.string.error_color_required else null

        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(colorCategoryId = col),
            //eventNameError = if (col == null) null else _addEditEventUIState.value.colorError
        )
    }

    override fun onNotificationDateChanged(date: Long?) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(notificationDate = date),
            //startDateError = null
        )
    }

    override fun onLocationChanged(value: LocationEntity) {
        _addEditEventUIState.value = _addEditEventUIState.value.copy(
            event = _addEditEventUIState.value.event.copy(location = value)
        )
    }


    override fun saveEvent() {

        if (_addEditEventUIState.value.event.eventName.isEmpty()) {
            _addEditEventUIState.value = _addEditEventUIState.value.copy(eventNameError = R.string.error_event_name_required)
        }
        else if (_addEditEventUIState.value.event.eventName.length > 100){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(eventNameError = R.string.error_event_name_too_long)

        }
        else if (_addEditEventUIState.value.event.eventDescription.isEmpty()){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(eventDescriptionError = R.string.error_event_description_required)
        }

        else if (_addEditEventUIState.value.event.eventDescription.length > 250){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(eventDescriptionError = R.string.error_event_description_too_long)
        }

        else if (_addEditEventUIState.value.event.colorCategoryId == null){

            _addEditEventUIState.value = _addEditEventUIState.value.copy(colorError = R.string.error_color_required)
        }

        else if (_addEditEventUIState.value.event.startDate == null){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(startDateError = R.string.can_t_be_empty)
        }
        else if (_addEditEventUIState.value.event.endDate == null){
            _addEditEventUIState.value = _addEditEventUIState.value.copy(endDateError = R.string.can_t_be_empty)
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