package com.example.projectobcane.navigation

import androidx.navigation.NavController
import com.example.projectobcane.database.LocationEntity


interface INavigationRouter {

    //other
    fun navigateToAddEditEvent(id: Long?, defaultStartDate: Long? = null)
    fun navigateToSettingsScreen()
    fun navigateToEventDetail(id: Long?)
    fun getNavController(): NavController
    fun navigateToAddEditColor(id: Long?)
    fun navigateToChoseLocation(latitude: Double?, longitude: Double?, locations: List<EventLocation>)
    fun returnFromMap(latitude: Double, longitude: Double)

    //both
    fun returnBack()
    fun navigateToMainScreen()

    //bottom
    fun navigateToRoute(route: String)



}