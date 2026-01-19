package com.example.projectobcane.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.settings.SettingsViewModel
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1(
    navigation: INavigationRouter
) {
    BaseScreen(
        topBarText = "",
    ) {
        OnBoardingScreen1Content(
            paddingValues = it,
            navigation = navigation
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen1Content(
    paddingValues: PaddingValues,
    navigation: INavigationRouter
) {
    // Title and Subtitle text
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = basicMargin, end = basicMargin, top = paddingValues.calculateTopPadding())
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.infoobce), // Replace with actual logo resource
            contentDescription = "ObecInfo Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = basicMargin)
        )

        // Title Text
        Text(
            text = "Vítejte v aplikaci ObecInfo",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = halfMargin)
        )

        // Subtitle Text
        Text(
            text = "Sledujte novinky z vaší obce",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(bottom = halfMargin)
        )

        // Continue Button
        Button(
            onClick = {
                navigation.navigateToOnBoarding2() // Define the navigation action to the next screen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.continue_button))
        }
    }
}

