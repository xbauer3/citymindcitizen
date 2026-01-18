package com.example.projectobcane.screens.reports.addEdit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.database.reports.ReportStatus
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditReportScreen(
    navigation: INavigationRouter,
    id: Long?,
    defaultStartDate : Long? = null
) {

    val viewModel = hiltViewModel<AddEditReportScreenViewModel>()
    val state = viewModel.addEditReportUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadReport(id)
    }


    if (state.value.reportSaved || state.value.reportDeleted){
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
                    viewModel.deleteReport()
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
    data: AddEditReportScreenUIState,
    actions: AddEditReportScreenActions,
    navigation: INavigationRouter,
    id: Long?

) {

    var expanded by remember { mutableStateOf(false) }



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
                value = data.report.title,
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
                value = data.report.description,
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
            OutlinedTextField(
                value = data.report.category,
                onValueChange = {
                    actions.onCategoryChanged(it)
                },
                isError = data.categoryError != null,
                supportingText = {
                    if (data.categoryError != null) {
                        Text(text = stringResource(data.categoryError!!))
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Category") },
                maxLines = 1,

                )

        }


        // status
        item {

            if (id != null) {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        value = data.report.status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ReportStatus.values().forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.label) },
                                onClick = {
                                    actions.onStatusChanged(status.value)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }else{
                LaunchedEffect(Unit) {
                    actions.onStatusChanged(ReportStatus.NEW.value)
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






        item{

            Button(onClick = {
                actions.saveReport()
            },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }

        }



    }


}




