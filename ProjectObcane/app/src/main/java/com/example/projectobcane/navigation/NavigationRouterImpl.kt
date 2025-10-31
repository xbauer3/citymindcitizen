package com.example.projectobcane.navigation

import androidx.navigation.NavController
import com.example.projectobcane.constants.Constants
import com.example.projectobcane.database.Event
import com.example.projectobcane.database.LocationEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.net.URLEncoder


class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun navigateToAddEditEvent(id: Long?, defaultStartDate: Long?) {
        //navController.navigate(Destination.AddEditTaskScreen.route + "/" + id)
        if (id != null) {
            navController.navigate("${Destination.EditEventScreen.route}/${id}")

        } else {
            navController.navigate("${Destination.AddEventScreen.route}/${defaultStartDate}")
        }
    }




    override fun navigateToSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
    }

    override fun navigateToEventDetail(id: Long?) {
        navController.navigate("${Destination.EventDetailScreen.route}/${id}")
    }


    override fun returnBack() {
        navController.popBackStack()
    }

    override fun getNavController(): NavController {
        return navController
    }

    override fun navigateToAddEditColor(id: Long?) {


        if (id != null) {
            navController.navigate("${Destination.AddEditColor.route}/${id}")
        } else {
            navController.navigate(Destination.AddEditColor.route)
        }

    }

    override fun navigateToMainScreen() {
        navController.navigate(Destination.MainScreenScreen.route)
    }





    override fun navigateToChoseLocation(
        latitude: Double?,
        longitude: Double?,
        locations: List<EventLocation>
    ) {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ChooseLocationDestination::class.java)
        val json = adapter.toJson(ChooseLocationDestination(latitude, longitude, locations))
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