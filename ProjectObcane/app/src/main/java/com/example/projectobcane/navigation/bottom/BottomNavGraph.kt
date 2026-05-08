package com.example.projectobcane.navigation.bottom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectobcane.navigation.INavigationRouter


import com.example.projectobcane.screens.chat.AiChatScreen
import com.example.projectobcane.screens.community.CommunityScreen
import com.example.projectobcane.screens.maps.MapsScreen
import com.example.projectobcane.screens.news.NewsScreen
import com.example.projectobcane.screens.reports.ReportsScreen


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
            NewsScreen(
                bottomNav = bottomNavRouter,
                rootNav = rootNavRouter,
                paddingValues = paddingValues
            )
        }

        composable(BottomBarScreen.AiAgent.route) {
            AiChatScreen(
                paddingValues = paddingValues,
                //rootNav = rootNavRouter
            )
        }

        composable(BottomBarScreen.Reports.route) {
            CommunityScreen(
                paddingValues = paddingValues
                /*
                bottomNav = bottomNavRouter,
                rootNav = rootNavRouter,
                paddingValues = paddingValues*/
            )
        }

        composable(BottomBarScreen.Maps.route) {
            MapsScreen(
                bottomNav = bottomNavRouter,
                rootNav = rootNavRouter,
                paddingValues = paddingValues
            )
        }
    }
}
