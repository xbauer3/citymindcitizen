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
import com.example.projectobcane.screens.SplashScreen
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








        //settings
        composable(route = Destination.SettingsScreen.route) {
            SettingsScreen(navRouter)
        }



        //splash screen
        composable(route = Destination.SplashScreen.route) {
            SplashScreen(navRouter)
        }



        //ADD Report
        composable(route = Destination.AddReportScreen.route) {
            AddEditReportScreen(navigation = navRouter, id = null)
        }


        //Edit Report

        composable(route = "${Destination.EditReportScreen.route}/{id}",
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


//Detail
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







    }

}