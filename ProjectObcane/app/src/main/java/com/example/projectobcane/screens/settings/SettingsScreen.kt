package com.example.projectobcane.screens.settings

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.screens.addEditScreen.AddEditEventActions
import com.example.projectobcane.screens.addEditScreen.AddEditEventUIState
import com.example.projectobcane.screens.addEditScreen.AddEditEventViewModel
import androidx.compose.runtime.remember
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.theme.basicMargin

import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<SettingsViewModel>()
    val state = viewModel.settingsScreenUIState.collectAsStateWithLifecycle()





    LaunchedEffect(Unit) {
        viewModel.loadSettings()
    }





    BaseScreen(
        topBarText = stringResource(R.string.settings),
        showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {/*
            if (id != null) {
                IconButton(onClick = {
                    //viewModel.deleteTravel()
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
            }*/
        }
    ) {
        SettingsScreenContent(
            paddingValues = it,
            viewModel = viewModel

            /*
            data = state.value,
            actions = viewModel*/
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel
    /*
    data: AddEditEventUIState,
    actions: AddEditEventActions*/
){

    val state = viewModel.settingsScreenUIState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity


    val expanded = remember { mutableStateOf(false) }



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

        val versionName = context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName

        Text(text = " ${stringResource(R.string.app_version)} $versionName")


        Spacer(modifier = Modifier.height(basicMargin))


        Text(text = stringResource(id = R.string.language)) // Add to strings.xml


        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {

            val currentLocale = state.value.supportedLanguages.find {
                it.language == state.value.selectedLanguage
            } ?: Locale.ENGLISH

            TextField(
                value = currentLocale.displayLanguage,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    TrailingIcon(expanded = expanded.value)
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                state.value.supportedLanguages.forEach { locale ->

                      DropdownMenuItem(
                        text = { Text(locale.displayLanguage) },
                        onClick = {
                            expanded.value = false
                            viewModel.updateLanguage(locale) {
                                activity?.recreate()
                            }
                        }
                    )
                }
            }
        }







    }





}


