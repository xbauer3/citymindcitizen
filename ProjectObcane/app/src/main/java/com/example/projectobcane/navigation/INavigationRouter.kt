package com.example.projectobcane.navigation

import androidx.navigation.NavController
import com.example.projectobcane.database.LocationEntity


interface INavigationRouter {
    fun navigateToAddEditEvent(id: Long?, defaultStartDate: Long? = null)
    fun navigateToSettingsScreen()
    fun navigateToEventDetail(id: Long?)

    fun returnBack()
    fun getNavController(): NavController

    fun navigateToAddEditColor(id: Long?)

    fun navigateToMainScreen()


    fun navigateToChoseLocation(latitude: Double?, longitude: Double?, locations: List<EventLocation>)
    fun returnFromMap(latitude: Double, longitude: Double)

}