package com.example.projectobcane.screens.addEditScreen

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.database.LocationEntity
import com.example.projectobcane.navigation.INavigationRouter

import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.InfoElement
import com.example.projectobcane.ui.elements.MyDatePicker
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.utils.DateUtils
import com.example.projectobcane.constants.Constants
import com.example.projectobcane.database.color.ColorCategory

import com.example.projectobcane.extensions.getValue
import com.example.projectobcane.extensions.removeValue
import com.example.projectobcane.navigation.EventLocation

import com.example.projectobcane.screens.colorC.ColorCategoryViewModel



import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    navigation: INavigationRouter,
    id: Long?,
    defaultStartDate : Long? = null
){

    val viewModel = hiltViewModel<AddEditEventViewModel>()
    val state = viewModel.addEditEventUIState.collectAsStateWithLifecycle()



    LaunchedEffect(Unit) {
        viewModel.initialize(id, defaultStartDate)
        viewModel.loadEventLocations()
    }




    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val mapLocationResult = navigation.getNavController().getValue<String>(Constants.LOCATION)
        mapLocationResult?.value?.let {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<LocationEntity> = moshi.adapter(LocationEntity::class.java)
            val location = jsonAdapter.fromJson(it)
            navigation.getNavController().removeValue<Double>(Constants.LOCATION)
            location?.let {
                viewModel.onLocationChanged(location)
            }
        }
    }



    if (state.value.eventSaved || state.value.eventDeleted){
        LaunchedEffect(state) {
            navigation.returnBack()
        }
    }


    BaseScreen(
        topBarText = stringResource(R.string.add_edit),
        showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if (id != null) {
                IconButton(onClick = {
                    viewModel.deleteEvent()
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
            }
        }
    ) {
        AddEditEventScreenContent(
            paddingValues = it,
            data = state.value,
            actions = viewModel,
            navigation = navigation,
            locations = state.value.eventLocations
        )
    }


}

enum class DatePickerType {
    START, END, NOTIFICATION
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreenContent(
    paddingValues: PaddingValues,
    data: AddEditEventUIState,
    actions: AddEditEventActions,
    navigation: INavigationRouter,
    locations: List<EventLocation>
     ){

    //val context = LocalContext.current


    val colorViewModel = hiltViewModel<ColorCategoryViewModel>()

    val state = colorViewModel.colorCategoryUIState.collectAsStateWithLifecycle()


    val currentDay = remember { mutableStateOf(LocalDateTime.now()) }


    //val colorCategories by colorViewModel.colorCategories.collectAsState()

    var selectedCategory by remember { mutableStateOf<ColorCategory?>(null) }


    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val activity = context as Activity





    LaunchedEffect(state.value.colorCategories, data.event.colorCategoryId) {
        val defaultColor = state.value.colorCategories.firstOrNull()
        if (data.event.colorCategoryId == null && defaultColor != null) {
            selectedCategory = defaultColor
            actions.onColorChanged(defaultColor.id)
        } else {
            selectedCategory = state.value.colorCategories.find { it.id == data.event.colorCategoryId }
        }
    }



    var showDatePickerFor by remember { mutableStateOf<DatePickerType?>(null) }


    if (showDatePickerFor != null) {
        MyDatePicker(
            date = when (showDatePickerFor) {
                DatePickerType.START -> data.event.startDate
                DatePickerType.END -> data.event.endDate
                DatePickerType.NOTIFICATION -> data.event.notificationDate
                null -> TODO()
            },
            onDateSelected = {
                when (showDatePickerFor) {
                    DatePickerType.START -> actions.onStartDateChanged(it)
                    DatePickerType.END -> actions.onEndDateChanged(it)
                    DatePickerType.NOTIFICATION -> actions.onNotificationDateChanged(it)
                    null -> TODO()
                }
            },
            onDismiss = { showDatePickerFor = null }
        )
    }



    LazyColumn(modifier = Modifier.padding(
        start = basicMargin,
        end = basicMargin,
        top = paddingValues.calculateTopPadding(),
        bottom = paddingValues.calculateBottomPadding()
    ))
    {

        // eventName


        item {
            OutlinedTextField(
                value = data.event.eventName,
                onValueChange = {
                    actions.onEventNameChanged(it)
                },
                isError = data.eventNameError != null,
                supportingText = {
                    if (data.eventNameError != null){
                        Text(text = stringResource(data.eventNameError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = stringResource(R.string.event_name))},
                maxLines = 1
            )
        }

        // eventDescription


        item {
            OutlinedTextField(
                value = data.event.eventDescription,
                onValueChange = {
                    actions.onEventDescriptionChanged(it)
                },
                isError = data.eventDescriptionError != null,
                supportingText = {
                    if (data.eventDescriptionError != null){
                        Text(text = stringResource(data.eventDescriptionError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = stringResource(R.string.event_description))},
                maxLines = 1,

            )

        }



//start date

        item {
            InfoElement(
                value = data.event.startDate?.let { DateUtils.getDateString(it) },
                hint = stringResource(R.string.start_date),
                isError = data.startDateError != null,
                supportingText = {
                    if (data.startDateError != null){
                        Text(text = stringResource(data.startDateError!!))
                    }
                },
                leadingIcon = Icons.Default.DateRange,
                onClick = { showDatePickerFor = DatePickerType.START },
                onClearClick = { actions.onStartDateChanged(null) }

                )
        }


// End Date

        item {
            InfoElement(
                value = data.event.endDate?.let { DateUtils.getDateString(it) },
                hint = stringResource(R.string.end_date),
                isError = data.endDateError != null,
                supportingText = {
                    if (data.endDateError != null){
                        Text(text = stringResource(data.endDateError!!))
                    }
                },
                leadingIcon = Icons.Default.DateRange,
                onClick = { showDatePickerFor = DatePickerType.END },
                onClearClick = { actions.onEndDateChanged(null) }
            )
        }




// Color

        item {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.color_category)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    isError = data.colorError != null,
                    supportingText = {
                        if (data.colorError != null){
                            Text(text = stringResource(data.colorError!!))
                        }
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.add_new_color)) },
                        onClick = {
                            expanded = false
                            navigation.navigateToAddEditColor(null) // <-- implement this screen
                        }
                    )

                    state.value.colorCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category
                                actions.onColorChanged(category.id)
                                expanded = false
                            }
                        )
                    }
                }
            }

        }


// Notification Date
        item {
            InfoElement(
                value = data.event.notificationDate?.let { DateUtils.getDateString(it) },
                hint = stringResource(R.string.notification_date),
                leadingIcon = Icons.Default.DateRange,
                onClick = { showDatePickerFor = DatePickerType.NOTIFICATION },
                onClearClick = { actions.onNotificationDateChanged(null) }
            )
        }


        // Location
        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigation.navigateToChoseLocation(
                            data.event.location.latitude,
                            data.event.location.longitude,
                            locations = locations

                        )
                    }
                    .padding(all = basicMargin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(halfMargin))
                Text(
                    text = "${stringResource(R.string.selected_location)}: ${data.event.location.latitude}, ${data.event.location.longitude}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                //Text(text = data.event.location?.address ?: "Choose Location")
            }
        }







        item{

            Button(onClick = {
                actions.saveEvent()
            },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }

        }



    }
}
