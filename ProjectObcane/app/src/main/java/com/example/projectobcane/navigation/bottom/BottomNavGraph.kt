package com.example.projectobcane.navigation.bottom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectobcane.navigation.ChooseLocationDestination
import com.example.projectobcane.navigation.Destination
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.navigation.NavigationRouterImpl
import com.example.projectobcane.screens.SplashScreen

import com.example.projectobcane.screens.events.EventsScreen
import com.example.projectobcane.screens.chat.AiChatScreen
import com.example.projectobcane.screens.reports.ReportsScreen
import com.squareup.moshi.Moshi
import java.net.URLDecoder





@Composable
fun BottomNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    bottomNavRouter: INavigationRouter,
    rootNavRouter: INavigationRouter,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {

        composable(BottomBarScreen.News.route) {
            EventsScreen(
                bottomNav = bottomNavRouter,
                rootNav = rootNavRouter,
                paddingValues = paddingValues
            )
        }

        composable(BottomBarScreen.AiAgent.route) {
            AiChatScreen(
                paddingValues = paddingValues,
                rootNav = rootNavRouter
            )
        }

        composable(BottomBarScreen.Reports.route) {
            ReportsScreen(
                bottomNav = bottomNavRouter,
                rootNav = rootNavRouter,
                paddingValues = paddingValues
            )
        }
    }
}
