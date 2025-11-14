package com.example.projectobcane.screens.reports.addEdit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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

){

    //var expanded by remember { mutableStateOf(false) }



    LazyColumn(modifier = Modifier.padding(
        start = basicMargin,
        end = basicMargin,
        top = paddingValues.calculateTopPadding(),
        bottom = paddingValues.calculateBottomPadding()
    ))
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
                    if (data.titleError != null){
                        Text(text = stringResource(data.titleError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = "Title")},
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
                    if (data.descriptionError != null){
                        Text(text = stringResource(data.descriptionError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = "Description")},
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
                    if (data.categoryError != null){
                        Text(text = stringResource(data.categoryError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = "Category")},
                maxLines = 1,

                )

        }



        // status


        item {
            OutlinedTextField(
                value = data.report.status,
                onValueChange = {
                    actions.onStatusChanged(it)
                },
                isError = data.statusError != null,
                supportingText = {
                    if (data.statusError != null){
                        Text(text = stringResource(data.statusError!!))
                    }

                }
                , modifier = Modifier.fillMaxWidth(),
                label = {Text(text = "Status")},
                maxLines = 1,

                )

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




