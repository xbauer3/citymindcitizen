package com.example.projectobcane.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.projectobcane.screens.OnBoardingScreen1
import com.example.projectobcane.screens.OnBoardingScreen2
import com.example.projectobcane.screens.SplashScreen
import com.example.projectobcane.screens.chLocation.ChooseLocationScreen
import com.example.projectobcane.screens.events.addEdit.AddEditEventScreen
import com.example.projectobcane.screens.events.detail.EventDetailScreen
import com.example.projectobcane.screens.mainScreen.MainScreenScreen
import com.example.projectobcane.screens.reports.addEdit.AddEditReportScreen
import com.example.projectobcane.screens.reports.detail.ReportDetailScreen


import com.example.projectobcane.screens.settings.SettingsScreen


import com.squareup.moshi.Moshi
import java.net.URLDecoder


@Composable
fun NavGraph(
    startDestination: String,
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
){

    NavHost(navController = navHostController, startDestination = startDestination) {



        //mainscreen
        composable(route = Destination.MainScreenScreen.route) {
            MainScreenScreen(navRouter)
        }


        //OnBoarding1
        composable(route = Destination.OnBoarding1.route) {
            OnBoardingScreen1(navRouter)
        }


        //OnBoarding2
        composable(route = Destination.OnBoarding2.route) {
            OnBoardingScreen2(navRouter)
        }






        /*
        //map
        composable(Destination.PickLocationScreen.route) {
            MapPickerScreen(navRouter)
        }*/
        //chose location



        composable(
            route = "choose_location_screen?data={data}",
            arguments = listOf(
                navArgument("data") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("data")
            val dest = encoded?.let {
                val json = URLDecoder.decode(it, "UTF-8")
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(ChooseLocationDestination::class.java)
                adapter.fromJson(json)
            }
            if (dest != null) {
                ChooseLocationScreen(navRouter, dest)
            } else {
                // handle missing data if needed
                ChooseLocationScreen(navRouter, ChooseLocationDestination())
            }
        }





        //settings
        composable(route = Destination.SettingsScreen.route) {
            SettingsScreen(navRouter)
        }



        //splash screen
        composable(route = Destination.SplashScreen.route) {
            SplashScreen(navRouter)
        }



        //ADD Report
        composable(route = Destination.AddEditReportScreen.route) {
            AddEditReportScreen(navigation = navRouter, id = null)
        }


        //Edit Report

        composable(route = "${Destination.AddEditReportScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            AddEditReportScreen(navRouter, id)
        }


//Detail report
        composable(route = "${Destination.ReportDetailScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            ReportDetailScreen(navRouter, id)
        }






        //ADD Event
        composable(route = Destination.AddEditEventScreen.route) {
            AddEditEventScreen(navigation = navRouter, id = null)
        }


        //Edit Event

        composable(route = "${Destination.AddEditEventScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            AddEditEventScreen(navRouter, id)
        }


//Detail event
        composable(route = "${Destination.EventDetailScreen.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            EventDetailScreen(navRouter, id)
        }


    }

}