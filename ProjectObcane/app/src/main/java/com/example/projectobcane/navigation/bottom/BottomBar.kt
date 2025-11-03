package com.example.projectobcane.navigation.bottom

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projectobcane.navigation.INavigationRouter




@Composable
fun BottomBar(navigation: INavigationRouter) {

    val navController = navigation.getNavController() // ✅ extract NavController once


    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Maps,
        BottomBarScreen.Notifications,
        BottomBarScreen.Reports
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination



    NavigationBar {
        items.forEach { screen ->

            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navigation = navigation
            )

        }
    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navigation: INavigationRouter
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            if (currentDestination?.hierarchy?.any{
                    it.route == screen.route
                } == true
            ){
                Icon(imageVector = screen.selectedIcon, contentDescription = "Selected Navigation Icon")
            }
            else{
                Icon(imageVector = screen.unselectedIcon, contentDescription = "UNSelected Navigation Icon")

            }
        },


        selected = currentDestination?.hierarchy?.any{
            it.route == screen.route
        } == true,
        onClick = {
            navigation.navigateToRoute(screen.route)
        }
    )
}