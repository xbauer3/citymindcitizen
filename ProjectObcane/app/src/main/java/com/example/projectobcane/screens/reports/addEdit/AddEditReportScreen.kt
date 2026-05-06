package com.example.projectobcane.screens.reports.addEdit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.Constants
import com.example.projectobcane.R
import com.example.projectobcane.database.reports.LocationEntity
import com.example.projectobcane.database.reports.ReportStatus
import com.example.projectobcane.extensions.removeValue
import com.example.projectobcane.navigation.Destination
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlin.collections.getValue
import com.example.projectobcane.extensions.getValue
import com.example.projectobcane.ui.elements.GlideImage
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditReportScreen(
    navigation: INavigationRouter,
    id: Long?,
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
    val context = LocalContext.current

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { uris ->
            uris.forEach {
                actions.onPhotoSelected(
                    context = context,
                    uri = it.toString()
                )
            }
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
                label = { Text(stringResource(R.string.title)) },
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
                label = { Text(text = stringResource(R.string.description)) },
                maxLines = 1,

                )

        }



        //Images



        item {
            Column {

                Button(
                    onClick = { photoPickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.add_photo))
                }

                if (data.images.isNotEmpty()) {
                    val pagerState = rememberPagerState(initialPage = 0) {
                        data.images.size // <- this replaces pageCount
                    }

                    Column {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) { page ->
                            GlideImage(
                                url = data.images[page],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(data.images.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (pagerState.currentPage == index)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }



        // category


        item {
            OutlinedTextField(
                value = data.report.reportType,
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
                label = { Text(text = stringResource(R.string.category)) },
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
                        label = { Text(stringResource(R.string.status)) },
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
                    value = ReportStatus.NEW.label,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.status)) }
                )

            }



        }



        //location


        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigation.navigateToChoseLocation(
                            data.report.location?.latitude,
                            data.report.location?.longitude,

                        )
                    }
                    .padding(all = basicMargin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(halfMargin))
                Text(
                    text = "${stringResource(R.string.selected_location)}: ${data.report.location?.latitude}, ${data.report.location?.longitude}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                //Text(text = data.event.location?.address ?: "Choose Location")
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




