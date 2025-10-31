package com.example.projectobcane.screens.colorC

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.screens.addEditScreen.AddEditEventScreenContent
import com.example.projectobcane.screens.addEditScreen.AddEditEventViewModel
import com.example.projectobcane.screens.addEditScreen.DatePickerType

import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.ColorInfoElement
import com.example.projectobcane.ui.elements.InfoElement
import com.example.projectobcane.ui.elements.parseHexColorOrWhite
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.ui.theme.heighOfARow
import com.example.projectobcane.utils.DateUtils
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent


@Composable
fun ColorCategoryScreen(
    navigation : INavigationRouter,
    //onNavigateBack: () -> Unit
){


    val viewModel = hiltViewModel<ColorCategoryViewModel>()

    val state = viewModel.colorCategoryUIState.collectAsStateWithLifecycle()



    //var name by remember { mutableStateOf("") }
    //var colorHex by remember { mutableStateOf("#FFFFFF") }
    //var priority by remember { mutableStateOf(1) }

    var id by remember { mutableStateOf(-1L) }


    val scrollState = rememberScrollState()

    val controller = rememberColorPickerController()


    /*
    LaunchedEffect(state.value.colorCategory.colorHex) {
        val colorInt = try {
            Color(android.graphics.Color.parseColor(state.value.colorCategory.colorHex))
        } catch (e: Exception) {
            Color.White
        }

        controller.setColor(colorInt)
    }*/

    val scope = rememberCoroutineScope()

    BaseScreen(

        topBarText = stringResource(R.string.color_management),
        //showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },

    ) {
        paddingValues ->


        Column(modifier = Modifier
            .padding(
                start = basicMargin,
                end = basicMargin,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .verticalScroll(scrollState)

        ) {
            /*
            Text("Add New Color Category", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))*/







            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.value.colorCategory.name,
                onValueChange = {

                    viewModel.onColorNameChanged(it)

/*
                    if (it.length < 50){
                        name = it
                    }*/

                },
                isError = state.value.colorNameError != null,
                supportingText = {
                    if (state.value.colorNameError != null){
                        Text(text = stringResource(state.value.colorNameError!!))
                    }
                },

                label = { Text(stringResource(R.string.name_color))},
                maxLines = 1

            )

            Spacer(modifier = Modifier.height(halfMargin))


            var showColorPicker by remember { mutableStateOf(false) }

            // Color picker

            ColorInfoElement(
                value = state.value.colorCategory.colorHex,
                hint = stringResource(R.string.color_hex),
                leadingIcon = Icons.Default.Create,
                onClick = {
                    showColorPicker = true
                },

                setShowColorPicker = { showColorPicker = it },
                isError = state.value.colorHexError != null,
                supportingText = {
                    if (state.value.colorHexError != null){
                        Text(text = stringResource(state.value.colorHexError!!))
                    }
                },

            )

            if (showColorPicker) {

                TextButton(
                    onClick = { showColorPicker = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.hide))
                }

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(basicMargin),
                    controller = controller,
                    onColorChanged = {
                        viewModel.onColorHexChanged(it.hexCode)
                    }
                )

                AlphaTile(
                    controller = controller,
                    modifier = Modifier
                        .height(heighOfARow)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                )



            }


            Spacer(modifier = Modifier.height(halfMargin))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = state.value.colorCategory.priority.toString(),
                onValueChange = { viewModel.onColorPriorityChanged(it) },
                label = { Text(stringResource(R.string.priority)) },
                isError = state.value.colorPriorityError != null,
                supportingText = {
                    if (state.value.colorPriorityError != null){
                        Text(text = stringResource(state.value.colorPriorityError!!))
                    }
                },
            )
            Spacer(modifier = Modifier.height(halfMargin))
            Button(onClick = {


                if (id == -1L) {
                    // Use coroutine scope to call suspend function
                    scope.launch {
                        val success = viewModel.saveColorCategory()

                        if (success) {
                            /*
                            viewModel.onColorNameChanged("")
                            viewModel.onColorHexChanged("#FFFFFF")
                            viewModel.onColorPriorityChanged("1")*/
                            id = -1L
                        }
                    }
                }
                else{
                    viewModel.saveColorCategory()
                }




            }) {
                Text(
                    if (id == -1L) stringResource(R.string.add_category)
                    else stringResource(R.string.edit_category)
                )
            }

            Spacer(modifier = Modifier.height(basicMargin))

            Text(stringResource(R.string.existing_categories), style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(halfMargin))


             // colorCategories.forEach { category ->
            state.value.colorCategories.forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {

                            viewModel.loadColorCategory(category)
                            id = category.id ?: -1L

                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    val color = parseHexColorOrWhite(category.colorHex)
                    Box(

                    modifier = Modifier
                        .size(basicMargin)
                        .background(color, CircleShape)
                        .padding(end = basicMargin)

                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = basicMargin)// Make text take up available space
                    ) {
                        Text("${category.name} (${category.colorHex})",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                        Text("${stringResource(R.string.priority)}: ${category.priority}", style = MaterialTheme.typography.bodySmall)
                    }


                    IconButton(onClick = { viewModel.deleteColorCategory(category) }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                    }




                }

            }







        }
    }








}

