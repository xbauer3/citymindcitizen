package com.example.projectobcane.screens.events.addEdit

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.Constants
import com.example.projectobcane.R
import com.example.projectobcane.database.events.EventCategory
import com.example.projectobcane.database.events.EventStatus
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.ReportStatus
import com.example.projectobcane.extensions.getValue
import com.example.projectobcane.extensions.removeValue
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenActions
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenUIState
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenViewModel
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.InfoElement
import com.example.projectobcane.ui.elements.MyDatePicker
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.utils.DateUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    navigation: INavigationRouter,
    id: Long?,
) {




    val viewModel = hiltViewModel<AddEditEventViewModel>()
    val state = viewModel.addEditEventUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadEvent(id)
    }


    if (state.value.eventSaved || state.value.eventDeleted){
        LaunchedEffect(state) {
            navigation.returnBack()
        }
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



    BaseScreen(
        topBarText = stringResource(R.string.add_edit_event),
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
        AddEditReportScreenContent(
            paddingValues = it,
            data = state.value,
            actions = viewModel,
            navigation = navigation,
            id = id
        )
    }


}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditReportScreenContent(
    paddingValues: PaddingValues,
    data: AddEditEventUIState,
    actions: AddEditEventActions,
    navigation: INavigationRouter,
    id: Long?

) {

    var expandedCategory by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }



    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker){
        MyDatePicker(
            date = data.event.date ?: System.currentTimeMillis(),
            onDateSelected = {
                actions.onDateChanged(it)
            },
            onDismiss = { showDatePicker = false }
        )
    }



    LazyColumn(
        modifier = Modifier.padding(
            start = basicMargin,
            end = basicMargin,
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    )
    {


        // title

        item {
            OutlinedTextField(
                value = data.event.title,
                onValueChange = {
                    actions.onTitleChanged(it)
                },
                isError = data.titleError != null,
                supportingText = {
                    if (data.titleError != null) {
                        Text(text = stringResource(data.titleError!!))
                    }

                }, modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Title") },
                maxLines = 1
            )
        }


        // description


        item {
            OutlinedTextField(
                value = data.event.description,
                onValueChange = {
                    actions.onDescriptionChanged(it)
                },
                isError = data.descriptionError != null,
                supportingText = {
                    if (data.descriptionError != null) {
                        Text(text = stringResource(data.descriptionError!!))
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Description") },
                maxLines = 1,

                )

        }

        // category


        item {

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = data.event.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    EventCategory.values().forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                actions.onCategoryChanged(category.name)
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

        }


        // status
        item {

            if (id != null) {

                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        value = data.event.status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = { expandedStatus = false }
                    ) {
                        EventStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name) },
                                onClick = {
                                    actions.onStatusChanged(status.name)
                                    expandedStatus = false
                                }
                            )
                        }
                    }
                }
            }else{
                LaunchedEffect(Unit) {
                    actions.onStatusChanged(EventStatus.UPCOMING.name)
                }

                OutlinedTextField(
                    value = ReportStatus.NEW.label, // always "New"
                    onValueChange = {},
                    readOnly = true,
                    enabled = false, // optional: visually disabled
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Status") }
                )

            }



        }

        //placeName
        item {
            OutlinedTextField(
                value = data.event.placeName,
                onValueChange = {
                    actions.onPlaceNameChanged(it)
                },
                isError = data.placeNameError != null,
                supportingText = {
                    if (data.placeNameError != null) {
                        Text(text = stringResource(data.placeNameError!!))
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Place Name") },
                maxLines = 1,

                )

        }




        // Notification Date
        item {
            InfoElement(
                value = data.event.date?.let { DateUtils.getDateString(it) },
                hint = stringResource(R.string.notification_date),
                leadingIcon = Icons.Default.DateRange,
                onClick = { showDatePicker = true },
                onClearClick = { actions.onDateChanged(null) }
            )
        }





        //location

        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigation.navigateToChoseLocation(
                            data.event.location.latitude,
                            data.event.location.longitude,

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



