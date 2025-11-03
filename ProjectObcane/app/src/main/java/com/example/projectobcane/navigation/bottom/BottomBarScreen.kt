package com.example.projectobcane.navigation.bottom


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.projectobcane.navigation.EventLocation
import com.example.projectobcane.navigation.INavigationRouter


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,


    //val action: (INavigationRouter) -> Unit // 👈 Instead of static route, use router action


) {

    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Default.Notifications,
        hasNews = false,
        badgeCount = null
    )
    object Notifications : BottomBarScreen(
        route = "notifications",
        title = "Notifications",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Default.Notifications,
        hasNews = false,
        badgeCount = 45
    )
    object Maps : BottomBarScreen(
        route = "maps",
        title = "Maps",
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Default.LocationOn,
        hasNews = false,
        badgeCount = null
    )
    object Reports : BottomBarScreen(
        route = "reports",
        title = "Reports",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Default.Build,
        hasNews = true,
        badgeCount = null
    )

}

