package com.example.projectobcane.navigation

sealed class Destination(val route: String) {
    object MainScreenScreen : Destination("main_screen")
    object SettingsScreen : Destination("settings_screen")


    object ReportDetailScreen : Destination("report_detail")

    object PickLocationScreen : Destination("pick_location")


    object AddEditReportScreen : Destination("add_edit")

    object SplashScreen: Destination("splash_screen")

}