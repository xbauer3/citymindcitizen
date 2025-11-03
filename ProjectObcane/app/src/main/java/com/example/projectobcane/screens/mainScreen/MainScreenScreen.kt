package com.example.projectobcane.screens.mainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectobcane.R
import com.example.projectobcane.database.EventWithColorCategory
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.navigation.NavigationRouterImpl
import com.example.projectobcane.navigation.bottom.BottomBar
import com.example.projectobcane.navigation.bottom.BottomBarScreen
import com.example.projectobcane.navigation.bottom.BottomNavGraph
import com.example.projectobcane.ui.elements.BaseScreen
import com.example.projectobcane.ui.elements.EventRow

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScreen(navigation: INavigationRouter) {

    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state = viewModel.mainScreenUIState.collectAsStateWithLifecycle()
    val events = state.value.events

    // create a NavController specifically for the bottom navigation
    val bottomNavController = rememberNavController()
    // wrap it into a router (so your BottomBar and graph can use it)
    val bottomNavRouter = remember { NavigationRouterImpl(bottomNavController) }

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    BaseScreen(
        topBarText = stringResource(R.string.events),
        actions = {
            IconButton(onClick = { navigation.navigateToSettingsScreen() }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
            }
        },
        bottomBar = { BottomBar(navigation = bottomNavRouter) } // ✅ bottom nav uses its own router
    ) { paddingValues ->
        MainScreenScreenContent(
            paddingValues = paddingValues,
            navigation = bottomNavRouter, // ✅ same router as the bottom bar
            navHostController = bottomNavController
        )
    }
}

@Composable
fun MainScreenScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    navHostController: NavHostController
) {
    // ✅ This now correctly connects the bottom navigation graph to the bottomNavRouter
    BottomNavGraph(
        startDestination = BottomBarScreen.Home.route,
        navHostController = navHostController,
        navRouter = navigation
    )
}



