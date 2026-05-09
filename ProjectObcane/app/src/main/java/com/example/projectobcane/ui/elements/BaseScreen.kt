package com.example.projectobcane.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.projectobcane.ui.theme.ColorWhite
import com.example.projectobcane.ui.theme.Purple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectobcane.R
import com.example.projectobcane.ui.theme.halfMargin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String,
    showTopBar: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    showLoading: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
){
    Scaffold(
        topBar = {
            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = topBarText)
                    },
                    navigationIcon = {
                        if (onBackClick != null) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.citymindicon),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).padding(start = halfMargin),
                            )
                        }
                    },
                    actions = actions,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple,
                        titleContentColor = ColorWhite,
                        navigationIconContentColor = ColorWhite,
                        actionIconContentColor = ColorWhite
                    )
                )
            }
        },
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar
    ) {
        if (showLoading){
            LoadingScreen()
        } else if (placeholderScreenContent != null){
            PlaceholderScreen(
                paddingValues = it,
                placeholderScreenContent = placeholderScreenContent)
        } else {
            content(it)
        }
    }

}