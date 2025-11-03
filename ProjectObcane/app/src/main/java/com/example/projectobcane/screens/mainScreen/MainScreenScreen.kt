package com.example.projectobcane.screens.mainScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.projectobcane.R
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.navigation.NavigationRouterImpl
import com.example.projectobcane.navigation.bottom.BottomBar
import com.example.projectobcane.navigation.bottom.BottomBarScreen
import com.example.projectobcane.navigation.bottom.BottomNavGraph
import com.example.projectobcane.ui.elements.BaseScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScreen(navigation: INavigationRouter) {

    // Create the NavController for the bottom navigation
    val bottomNavController = rememberNavController()
    val bottomNavRouter = remember { NavigationRouterImpl(bottomNavController) }

    // Scaffold or your BaseScreen wrapper with BottomBar
    BaseScreen(
        topBarText = stringResource(R.string.events),
        actions = {
            IconButton(onClick = { navigation.navigateToSettingsScreen() }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
            }
        },
        bottomBar = { BottomBar(navigation = bottomNavRouter) }
    ) { paddingValues ->
        BottomNavGraph(
            startDestination = BottomBarScreen.Home.route,
            navHostController = bottomNavController,
            navRouter = bottomNavRouter
        )
    }
}



