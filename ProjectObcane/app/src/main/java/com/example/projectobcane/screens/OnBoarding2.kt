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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.settings.LanguageHolder
import com.example.projectobcane.screens.settings.LanguagePreferences
import com.example.projectobcane.screens.settings.SettingsViewModel
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.utils.OnboardingPreferences
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.runtime.rememberCoroutineScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen2(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<SettingsViewModel>()
    val state = viewModel.settingsScreenUIState.collectAsStateWithLifecycle()





    LaunchedEffect(Unit) {
        viewModel.loadSettings()
    }





    BaseScreen(
        topBarText = stringResource(R.string.pick_your_language),

    ) {
        OnBoardingScreen2Content(
            paddingValues = it,
            viewModel = viewModel,
            navigation = navigation
        )
    }



}



@Composable
fun OnBoardingScreen2Content(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel,
    navigation: INavigationRouter
) {
    val state = viewModel.settingsScreenUIState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as Activity

    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painterResource(id = R.drawable.flags),
        contentDescription = "ObecInfo Logo",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = basicMargin)
    )

    Column(
        modifier = Modifier
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(basicMargin))

        state.value.supportedLanguages.forEach { locale ->
            Button(
                onClick = {
                    viewModel.updateLanguage(locale) {
                        coroutineScope.launch {
                            OnboardingPreferences.setCompleted(context)
                            activity.recreate()
                            navigation.navigateToMainScreen()
                        }
                    }
                }
            ) {
                Text(text = locale.displayLanguage)
            }
        }
    }
}

