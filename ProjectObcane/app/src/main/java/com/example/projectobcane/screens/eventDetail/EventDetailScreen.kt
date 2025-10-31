package com.example.projectobcane.screens.detailScreen

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.example.projectobcane.R
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.elements.BaseScreen
//import com.example.projectobcane.ui.elements.DeleteButton
import com.example.projectobcane.ui.elements.GlideImage
import com.example.projectobcane.ui.elements.parseHexColorOrWhite
import com.example.projectobcane.ui.theme.basicMargin
import com.example.projectobcane.ui.theme.halfMargin
import com.example.projectobcane.utils.DateUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navigation: INavigationRouter,
    id: Long?
){

    val viewModel = hiltViewModel<EventDetailViewModel>()
    val state = viewModel.eventDetailUIState.collectAsStateWithLifecycle()



    LaunchedEffect(id) {
        viewModel.loadEvent(id)
    }





    BaseScreen(

        topBarText = stringResource(R.string.event_detail),
        showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if (id != null) {



                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = stringResource(R.string.delete_event)) },
                        text = { Text(text = stringResource(R.string.confirm_delete)) },
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

                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }



            }
        }
    ) {
        EventDetailScreenContent(
            paddingValues = it,
            data = state.value,
            navigation = navigation

        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreenContent(
    paddingValues: PaddingValues,
    data: EventDetailUIState,
    navigation: INavigationRouter
){





    LazyColumn(modifier = Modifier.padding(
        start = basicMargin,
        end = basicMargin,
        top = paddingValues.calculateTopPadding(),
        bottom = paddingValues.calculateBottomPadding()
    ))
    {
        item{
            Column {

                GlideImage(
                    url = "https://picsum.photos/400?random=${data.event.id ?: 0}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )


                Text(data.event.eventName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(halfMargin))
                Text(data.event.eventDescription)
                Spacer(modifier = Modifier.height(halfMargin))

                Row {
                    Text(data.event.startDate?.let { DateUtils.getDateString(it) } ?: "")
                    Text((" - " + data.event.endDate?.let { DateUtils.getDateString(it) }) ?: "")
                }

                Spacer(modifier = Modifier.height(halfMargin))



                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    val color = parseHexColorOrWhite(data.colorCategory?.colorHex)
                    Box(

                        modifier = Modifier
                            .size(basicMargin)
                            .background(color, CircleShape)

                    )

                    //Text("${data.colorCategory?.name} (${data.colorCategory?.colorHex}) - ${stringResource(R.string.priority)}: ${data.colorCategory?.priority}")

                    Spacer(modifier = Modifier.width(basicMargin))



                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = basicMargin)// Make text take up available space
                    ) {
                        Text("${data.colorCategory?.name} (${data.colorCategory?.colorHex})",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis)
                        Text("${stringResource(R.string.priority)}: ${data.colorCategory?.priority}", style = MaterialTheme.typography.bodySmall)
                    }


                }


                Spacer(modifier = Modifier.height(halfMargin))

                Button(onClick = {
                        navigation.navigateToAddEditEvent(id = data.event.id)
                    },
                    modifier = Modifier.fillMaxWidth()


                ) {

                    Text(stringResource(R.string.edit))
                }






            }
        }


    }



}
