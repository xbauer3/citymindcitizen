package com.example.projectobcane.screens.chLocation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.screens.addEditScreen.AddEditEventActions
import com.example.projectobcane.screens.addEditScreen.AddEditEventUIState
import com.example.projectobcane.screens.addEditScreen.AddEditEventViewModel

import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

import com.google.maps.android.compose.*
import com.example.projectobcane.R
import androidx.compose.ui.res.stringResource
import com.example.projectobcane.navigation.ChooseLocationDestination
import com.example.projectobcane.navigation.EventLocation
import com.example.projectobcane.ui.theme.basicMargin
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.example.projectobcane.ui.theme.halfMargin
import com.google.android.gms.maps.model.BitmapDescriptorFactory


@Composable
fun ChooseLocationScreen(
    navigation: INavigationRouter,
    chooseLocationDestination: ChooseLocationDestination
) {
    val viewModel: ChooseLocationViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val eventLocations by viewModel.eventLocations.collectAsState()

    LaunchedEffect(Unit) {
        if (chooseLocationDestination.latitude != null && chooseLocationDestination.longitude != null) {
            viewModel.locationChanged(chooseLocationDestination.latitude!!, chooseLocationDestination.longitude!!)
        }
    }



    BaseScreen(
        topBarText = stringResource(R.string.map),
        onBackClick = { navigation.returnBack() },
        content = {
            ChooseLocationContent(
                paddingValues = it,
                latitude = state.latitude,
                longitude = state.longitude,
                eventLocations = eventLocations,
                actions = viewModel,
                onButtonClick = {
                    if (state.locationChanged) {
                        navigation.returnFromMap(state.latitude, state.longitude)
                    } else {
                        navigation.returnBack()
                    }
                }
            )
        }
    )
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ChooseLocationContent(
    paddingValues: PaddingValues,
    latitude: Double,
    longitude: Double,
    eventLocations : List<EventLocation>,
    actions: ChooseLocationActions,
    onButtonClick: () -> Unit
) {
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                actions.locationChanged(latLng.latitude, latLng.longitude)
            }
        ) {
            MapEffect(Unit) { map ->
                map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDrag(p0: Marker) {}
                    override fun onMarkerDragStart(p0: Marker) {}
                    override fun onMarkerDragEnd(p0: com.google.android.gms.maps.model.Marker) {
                        actions.locationChanged(p0.position.latitude, p0.position.longitude)
                    }
                })
            }

            Marker(
                state = MarkerState(position = LatLng(latitude, longitude)),
                draggable = true
            )


            // Draw each event location
            eventLocations
                .filter { it.latitude != 0.0 && it.longitude != 0.0 }
                .forEach { location ->
                    val latLng = LatLng(location.latitude, location.longitude)

                    Circle(
                        center = latLng,
                        radius = 200.0,
                        strokeColor = Color.Blue.copy(alpha = 0.5f),
                        fillColor = Color.Blue.copy(alpha = 0.2f)
                    )

                    Marker(
                        state = MarkerState(position = latLng),
                        title = location.name,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                        draggable = false
                    )
                }



        }

        Box(modifier = Modifier
            .padding(halfMargin)
            .align(Alignment.TopCenter)) {
            MarkerHelp()
        }
        OutlinedButton(
            onClick = onButtonClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(basicMargin),
            shape = RoundedCornerShape(basicMargin),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.save_location))
        }
    }
}

@Composable
fun MarkerHelp(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(halfMargin),
        elevation = CardDefaults.cardElevation(halfMargin)
    ) {
        Text(
            modifier = Modifier.padding(halfMargin),
            text = stringResource(R.string.marker_help),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}