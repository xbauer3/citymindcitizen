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
import com.example.projectobcane.screens.addEditScreen.AddEditEventScreen
import com.example.projectobcane.screens.mainScreen.MainScreenScreen



import com.example.projectobcane.screens.settings.SettingsScreen

import com.example.projectobcane.screens.chLocation.ChooseLocationScreen
import com.example.projectobcane.screens.colorC.ColorCategoryScreen
import com.example.projectobcane .screens.detailScreen.EventDetailScreen
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


        //edit event
        composable(route = "${Destination.EditEventScreen.route}/{id}",
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

        //add event
        composable(route = "${Destination.AddEventScreen.route}/{defaultStartDate}",
            arguments = listOf(
                navArgument("defaultStartDate"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val defaultStartDate = it.arguments?.getLong("defaultStartDate")
            AddEditEventScreen(navigation = navRouter, id = null, defaultStartDate = defaultStartDate)
        }



        //event detail

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



        //settings
        composable(route = Destination.SettingsScreen.route) {
            SettingsScreen(navRouter)
        }



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








        //choose color
        composable(route = Destination.AddEditColor.route) {
            //ColorCategoryScreen(navRouter, null)
            ColorCategoryScreen(navRouter)
        }





        //splash screen
        composable(route = Destination.SplashScreen.route) {
            SplashScreen(navRouter)
        }





        /*
        //edit color
        composable(route = "${Destination.AddEditColor.route}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ){
            val id = it.arguments?.getLong("id")
            ColorCategoryScreen(navRouter, id)
        }

*/


    }

}