package com.example.projectobcane.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


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
                TopAppBar(
                    title = { Text(text = topBarText) },
                    navigationIcon = {
                        if (onBackClick != null) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    actions = actions
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