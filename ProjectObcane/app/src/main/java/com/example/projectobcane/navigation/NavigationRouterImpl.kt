package com.example.projectobcane.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.projectobcane.constants.Constants
import com.example.projectobcane.database.reports.LocationEntity

import com.example.projectobcane.navigation.bottom.BottomBarScreen
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.net.URLEncoder


class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {



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
            navController.navigate("${Destination.AddEditReportScreen.route}/${id}")

        } else {
            navController.navigate(Destination.AddEditReportScreen.route)
        }


    }
/*
    override fun navigateToPickLocation() {
        navController.navigate(Destination.PickLocationScreen.route)
    }*/


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






    override fun navigateToChoseLocation(
        latitude: Double?,
        longitude: Double?,

    ) {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ChooseLocationDestination::class.java)
        val json = adapter.toJson(ChooseLocationDestination(latitude, longitude))
        val encoded = URLEncoder.encode(json, "UTF-8")
        navController.navigate("choose_location_screen?data=$encoded")
    }



    override fun returnFromMap(latitude: Double, longitude: Double) {
        val moshi: Moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<LocationEntity> = moshi.adapter(LocationEntity::class.java)

        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set(Constants.LOCATION, jsonAdapter.toJson(LocationEntity(latitude, longitude)))
        returnBack()
    }



}