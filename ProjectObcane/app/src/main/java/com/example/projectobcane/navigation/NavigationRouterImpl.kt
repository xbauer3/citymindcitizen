package com.example.projectobcane.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.projectobcane.constants.Constants

import com.example.projectobcane.navigation.bottom.BottomBarScreen
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.net.URLEncoder


class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToAddEditEvent(id: Long?) {
        //navController.navigate(Destination.AddEditTaskScreen.route + "/" + id)
        if (id != null) {
            navController.navigate("${Destination.EditReportScreen.route}/${id}")

        } else {
            navController.navigate("${Destination.AddReportScreen.route}/${id}")
        }
    }

    override fun navigateToSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
    }

    override fun navigateToEventDetail(id: Long?) {
        navController.navigate("${Destination.ReportDetailScreen.route}/${id}")
    }


    override fun getNavController(): NavController {
        return navController
    }





    override fun navigateToAddEditReport(id: Long?) {
        if (id != null) {
            navController.navigate("edit_report/$id")
        } else {
            navController.navigate("add_report")
        }
    }




    //both

    override fun navigateToMainScreen() {
        navController.navigate(Destination.MainScreenScreen.route){
            popUpTo(Destination.SplashScreen.route) { inclusive = true }
        }
    }

    override fun returnBack() {
        navController.popBackStack()
    }




    //bottom

    override fun navigateToRoute(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }






}