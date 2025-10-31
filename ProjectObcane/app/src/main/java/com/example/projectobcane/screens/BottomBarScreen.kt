package com.example.projectobcane.screens


import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.projectobcane.navigation.EventLocation
import com.example.projectobcane.navigation.INavigationRouter


sealed class BottomBarScreen(
    val title: String,
    val icon: ImageVector,
    val action: (INavigationRouter) -> Unit // 👈 Instead of static route, use router action
) {
    object Home : BottomBarScreen(
        title = "Home",
        icon = Icons.Default.Notifications,
        action = { it.navigateToMainScreen() }
    )

    object Events : BottomBarScreen(
        title = "Add Event",
        icon = Icons.Default.DateRange,
        action = { it.navigateToSettingsScreen() }
    )

    object Map : BottomBarScreen(
        title = "Settings",
        icon = Icons.Default.LocationOn,
        action = { it.navigateToChoseLocation(latitude = 15.5, longitude = 14.4, listOf( EventLocation(latitude = 0.0, longitude = 0.0, name = "Default Location")))}
    )

    object Reports : BottomBarScreen(
        title = "Colors",
        icon = Icons.Default.Build,
        action = { it.navigateToAddEditColor(id = null) }
    )
}


@Composable
fun BottomNavigationBar(navController: INavigationRouter) {
    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Events,
        BottomBarScreen.Map,
        BottomBarScreen.Reports
    )
    val navBackStackEntry by navController.getNavController().currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->



            NavigationBarItem(
                selected = false,
                onClick = {
                    screen.action(navController)
                },
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) }
            )
        }
    }
}