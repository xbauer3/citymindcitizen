package com.example.projectobcane.screens.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.ReportsScreenViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap


@Composable
fun MapsScreen(navigation: INavigationRouter, paddingValues: PaddingValues) {

        val viewModel = hiltViewModel<ReportsScreenViewModel>()
        val state = viewModel.reportScreenUIState.collectAsStateWithLifecycle()

        GoogleMap(
            modifier = Modifier.fillMaxSize()
        ) {
            state.value.reports.forEach { report ->
                if (report.location.latitude != null && report.location.longitude != null) {
                    Circle(
                        center = LatLng(report.location.latitude!!, report.location.longitude!!),
                        radius = 50.0,
                        fillColor = Color.Cyan,
                        strokeColor = Color.Yellow,
                        clickable = true,
                        onClick = {
                            navigation.navigateToEventDetail(report.id)
                        }
                    )
                }
            }
        }
}
