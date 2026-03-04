package com.example.projectobcane.screens.reports.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.R
import com.example.projectobcane.ui.elements.GlideImage
import com.example.projectobcane.ui.elements.parseHexColorOrWhite
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.utils.DateUtils


import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    navigation: INavigationRouter,
    id: Long?
) {
    val viewModel = hiltViewModel<ReportDetailViewModel>()
    val state = viewModel.eventDetailUIState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadEvent(id)
    }

    BaseScreen(
        topBarText = stringResource(R.string.report_detail),
        showLoading = state.value.loading,
        onBackClick = { navigation.returnBack() },
        actions = {
            if (id != null) {
                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(stringResource(R.string.delete_report)) },
                        text = { Text(stringResource(R.string.confirm_delete)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteEvent()
                                showDialog = false
                                navigation.returnBack()
                            }) {
                                Text(stringResource(android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(stringResource(android.R.string.cancel))
                            }
                        }
                    )
                }

                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                }
            }
        }
    ) {
        ReportDetailContent(
            paddingValues = it,
            data = state.value,
            navigation = navigation
        )
    }
}


@Composable
fun ReportDetailContent(
    paddingValues: PaddingValues,
    data: ReportDetailUIState,
    navigation: INavigationRouter
) {
    val report = data.report

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.spacedBy(basicMargin)
    ) {

        // IMAGE
        item {

            if (data.images.isNotEmpty()) {

                LazyRow {
                    items(data.images) { uri ->
                        GlideImage(
                            url = uri,
                            modifier = Modifier
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }

            }
        }

        // TITLE + DESCRIPTION
        item {
            Column {
                Text(
                    text = report.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(halfMargin))

                Text(
                    text = report.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // REPORT INFO
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(basicMargin)) {

                    Text(
                        text = stringResource(R.string.report_information),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(halfMargin))

                    DetailRow(
                        label = stringResource(R.string.category),
                        value = report.reportType
                    )

                    DetailRow(
                        label = stringResource(R.string.status),
                        value = report.status
                    )

                    DetailRow(
                        label = stringResource(R.string.created_at),
                        value = report.createdAt?.let { DateUtils.getDateString(it) } ?: "-"
                    )
                }
            }
        }

        // LOCATION
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(basicMargin)) {
                    Text(
                        text = stringResource(R.string.location),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(halfMargin))

                    DetailRow(
                        label = stringResource(R.string.latitude),
                        value = report.location?.latitude.toString()
                    )

                    DetailRow(
                        label = stringResource(R.string.longitude),
                        value = report.location?.longitude.toString()
                    )
                }
            }
        }

        // ACTION BUTTONS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(halfMargin)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navigation.navigateToAddEditReport(id = report.localId)
                    }
                ) {
                    Text(stringResource(R.string.edit))
                }

            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}


