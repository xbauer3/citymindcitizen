package com.example.projectobcane.navigation.bottom

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projectobcane.navigation.INavigationRouter
import com.example.projectobcane.ui.theme.Purple

@Composable
fun BottomBar(navigation: INavigationRouter) {
    val navController = navigation.getNavController()

    val items = listOf(
        BottomBarScreen.News,
        BottomBarScreen.AiAgent,
        BottomBarScreen.Reports,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
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
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val tint = if (selected) Purple else MaterialTheme.colorScheme.onSurfaceVariant

    NavigationBarItem(
        label = {
            Text(
                text = stringResource(screen.title),
                color = tint
            )
        },
        icon = {
            BadgedBox(
                badge = {
                    if (screen.badgeCount != null) {
                        Badge { Text(text = screen.badgeCount.toString()) }
                    } else if (screen.hasNews) {
                        Badge()
                    }
                }
            ) {
                Icon(
                    imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                    contentDescription = null,
                    tint = tint
                )
            }
        },
        selected = selected,
        onClick = { navigation.navigateToRoute(screen.route) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Transparent,
            unselectedIconColor = Color.Transparent,
            selectedTextColor = Color.Transparent,
            unselectedTextColor = Color.Transparent,
            indicatorColor = Color.Transparent
        )
    )
}