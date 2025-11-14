package com.example.projectobcane.navigation

import androidx.navigation.NavController


interface INavigationRouter {

    //other
    fun navigateToAddEditEvent(id: Long?)
    fun navigateToSettingsScreen()
    fun navigateToEventDetail(id: Long?)
    fun getNavController(): NavController


    fun navigateToAddEditReport(id: Long?)


    //both
    fun returnBack()
    fun navigateToMainScreen()

    //bottom
    fun navigateToRoute(route: String)



}