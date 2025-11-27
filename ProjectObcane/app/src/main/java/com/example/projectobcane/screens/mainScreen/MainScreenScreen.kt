package com.example.projectobcane.screens.mainScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
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

    val bottomNavController = rememberNavController()
    val bottomNavRouter = remember { NavigationRouterImpl(bottomNavController) }

    val navBackStackEntry = bottomNavController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route


    val fabVisible = currentRoute == BottomBarScreen.Reports.route

    val currentTopBarTitle = when (currentRoute) {
        BottomBarScreen.Reports.route -> R.string.reports
        BottomBarScreen.Home.route -> R.string.home
        BottomBarScreen.Maps.route -> R.string.maps
        BottomBarScreen.Notifications.route -> R.string.notifications
        else -> R.string.app_name
    }



    BaseScreen(
        topBarText = stringResource(currentTopBarTitle),
        actions = {
            IconButton(onClick = { navigation.navigateToSettingsScreen() }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
            }
        },
        bottomBar = { BottomBar(navigation = bottomNavRouter) },
        floatingActionButton = {
            if (fabVisible) {
                FloatingActionButton(
                    onClick = { navigation.navigateToAddEditReport(null) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Report")
                }
            }
        }
    ) { paddingValues ->
        BottomNavGraph(
            startDestination = BottomBarScreen.Home.route,
            navHostController = bottomNavController,
            navRouter = bottomNavRouter,
            paddingValues = paddingValues
        )
    }
}




