package com.example.projectobcane.screens.reports.detail

import androidx.compose.foundation.background
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
        topBarText = stringResource(R.string.event_detail),
        showLoading = state.value.loading,
        onBackClick = { navigation.returnBack() },
        actions = {
            if (id != null) {
                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(stringResource(R.string.delete_event)) },
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
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
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
    LazyColumn(
        modifier = Modifier.padding(
            start = basicMargin,
            end = basicMargin,
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        item {
            val report = data.report
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = halfMargin),
                shape = RoundedCornerShape(basicMargin),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(basicMargin)
                ) {
                    // Image
                    GlideImage(
                        url = if (report.photoUri.isNotEmpty()) report.photoUri
                        else "https://picsum.photos/400?random=${report.id ?: 0}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(basicMargin))
                    )

                    Spacer(modifier = Modifier.height(basicMargin))

                    // Title
                    Text(
                        text = report.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(halfMargin))

                    // Description
                    Text(
                        text = report.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(halfMargin))

                    // Created date
                    Text(
                        text = report.createdAt?.let { DateUtils.getDateString(it) } ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(basicMargin))

                    // Edit button
                    Button(
                        onClick = { navigation.navigateToAddEditReport(id = report.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.edit))
                    }
                }
            }
        }
    }
}
