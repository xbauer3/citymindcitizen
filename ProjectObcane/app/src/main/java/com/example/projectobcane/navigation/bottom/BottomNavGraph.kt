package com.example.projectobcane.navigation.bottom

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
import com.example.projectobcane.screens.addEditScreen.AddEditEventScreen
import com.example.projectobcane.screens.chLocation.ChooseLocationScreen
import com.example.projectobcane.screens.colorC.ColorCategoryScreen
import com.example.projectobcane.screens.detailScreen.EventDetailScreen
import com.example.projectobcane.screens.events.EventsScreen
import com.example.projectobcane.screens.mainScreen.MainScreenScreen
import com.example.projectobcane.screens.maps.MapsScreen
import com.example.projectobcane.screens.notifications.NotificationsScreen
import com.example.projectobcane.screens.reports.ReportsScreen
import com.example.projectobcane.screens.settings.SettingsScreen
import com.squareup.moshi.Moshi
import java.net.URLDecoder





@Composable
fun BottomNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    navRouter: INavigationRouter
){
    NavHost(navController = navHostController, startDestination = startDestination) {



        //mainscreen
        composable(route = BottomBarScreen.Home.route) {
            EventsScreen(navRouter)
        }


        //notifications
        composable(route = BottomBarScreen.Notifications.route) {
            MapsScreen(navRouter)
        }


        //maps
        composable(route = BottomBarScreen.Maps.route) {
            NotificationsScreen(navRouter)
        }


        //reports
        composable(route = BottomBarScreen.Reports.route) {
            ReportsScreen(navRouter)
        }


    }

}