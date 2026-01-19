package com.example.projectobcane.navigation

import androidx.navigation.NavController


interface INavigationRouter {

    //other

    fun navigateToSettingsScreen()
    fun navigateToEventDetail(id: Long?)
    fun getNavController(): NavController



    fun navigateToAddEditReport(id: Long?)

   //fun navigateToPickLocation()


    fun navigateToChoseLocation(latitude: Double?, longitude: Double?)
    fun returnFromMap(latitude: Double, longitude: Double)


    //both
    fun returnBack()
    fun navigateToMainScreen()

    //bottom
    fun navigateToRoute(route: String)



}