package com.example.projectobcane.navigation

sealed class Destination(val route: String) {
    object MainScreenScreen : Destination("main_screen")
    object SettingsScreen : Destination("settings_screen")

    object AddReportScreen : Destination("add_report")
    object EditReportScreen : Destination ("edit_report")

    object ReportDetailScreen : Destination("report_detail")




    object SplashScreen: Destination("splash_screen")

}