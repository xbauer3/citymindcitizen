package com.example.projectobcane.navigation

sealed class Destination(val route: String) {
    object MainScreenScreen : Destination("main_screen")
    object SettingsScreen : Destination("settings_screen")

    object AddEventScreen : Destination("add_edit")
    object EditEventScreen : Destination ("edit_event")

    object EventDetailScreen : Destination("event_detail")
    object AddEditColor : Destination("add_color")

    object SplashScreen: Destination("splash_screen")

}