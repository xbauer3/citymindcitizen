package com.example.projectobcane.ui.elements


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreenViewModel
import com.example.projectobcane.ui.theme.basicMargin
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.projectobcane.navigation.Destination


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapPickerScreen(
    navigation: INavigationRouter
) {
    val navController = navigation.getNavController()

    val viewModel = sharedViewModel<AddEditReportScreenViewModel>(
        navController = navController,
        route = Destination.AddReportScreen.route
    )



    val state by viewModel.addEditReportUIState.collectAsStateWithLifecycle()

    val latitude = state.report.latitude ?: 49.20963543403347
    val longitude = state.report.longitude ?: 16.614246410843442

    val markerState = rememberMarkerState(
        position = LatLng(latitude, longitude)
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerState.position = latLng
                viewModel.onLocationPicked(
                    lat = latLng.latitude,
                    lng = latLng.longitude
                )


            }
        ) {

            // Marker
            Marker(
                state = markerState,
                draggable = true
            )

            // Drag listener
            MapEffect(Unit) { map ->
                map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDrag(marker: com.google.android.gms.maps.model.Marker) {}

                    override fun onMarkerDragStart(marker: com.google.android.gms.maps.model.Marker) {}

                    override fun onMarkerDragEnd(marker: com.google.android.gms.maps.model.Marker) {
                        viewModel.onLocationPicked(
                            marker.position.latitude,
                            marker.position.longitude
                        )
                    }
                })
            }
        }

        // Save Button

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(basicMargin),
            onClick = {
                navigation.returnBack()
            }
        ) {
            Text("Confirm Location")
        }
    }
}




@Composable
inline fun <reified VM : ViewModel> sharedViewModel(
    navController: NavController,
    route: String
): VM {
    val navBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(route)
    }
    return hiltViewModel(navBackStackEntry)
}

