package com.example.projectobcane.screens.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.ReportsScreenViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue


@Composable
fun MapsScreen(
    bottomNav: INavigationRouter,
    rootNav: INavigationRouter,
    paddingValues: PaddingValues
) {
    val viewModel = hiltViewModel<MapScreenViewModel>()
    val items by viewModel.items.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMapItems()
    }

    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(49.2096, 16.6142), // Brno
            12f
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        cameraPositionState = cameraPositionState
    ) {
        MapEffect(items) { googleMap ->

            val clusterManager = ClusterManager<MapItem>(
                context,
                googleMap
            )

            val renderer = MapClusterRenderer(
                context,
                googleMap,
                clusterManager
            )
            clusterManager.renderer = renderer

            googleMap.setOnCameraIdleListener(clusterManager)
            googleMap.setOnMarkerClickListener(clusterManager)

            clusterManager.clearItems()
            clusterManager.addItems(items)
            clusterManager.cluster()

            clusterManager.setOnClusterItemClickListener { item ->
                when (item) {
                    is MapItem.ReportItem ->
                        rootNav.navigateToReportDetail(item.reportId)

                    is MapItem.EventItem ->
                        rootNav.navigateToEventDetail(item.eventId)
                }
                true
            }
        }

    }
}

