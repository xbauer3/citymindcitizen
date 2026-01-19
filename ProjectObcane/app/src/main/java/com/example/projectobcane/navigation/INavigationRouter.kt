package com.example.projectobcane.navigation

import androidx.navigation.NavController


interface INavigationRouter {

    //other

    fun navigateToSettingsScreen()
    fun navigateToReportDetail(id: Long?)
    fun getNavController(): NavController



    fun navigateToAddEditReport(id: Long?)

   //fun navigateToPickLocation()


    fun navigateToChoseLocation(latitude: Double?, longitude: Double?)
    fun returnFromMap(latitude: Double, longitude: Double)

    fun navigateToAddEditEvent(id: Long?)
    fun navigateToEventDetail(id: Long?)

    //both
    fun returnBack()
    fun navigateToMainScreen()
    fun navigateToOnBoarding1()
    fun navigateToOnBoarding2()


    //bottom
    fun navigateToRoute(route: String)



}