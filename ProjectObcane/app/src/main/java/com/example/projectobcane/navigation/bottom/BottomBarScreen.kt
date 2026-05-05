package com.example.projectobcane.navigation.bottom


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.projectobcane.R
import com.example.projectobcane.navigation.EventLocation
import com.example.projectobcane.navigation.INavigationRouter
import androidx.compose.ui.res.stringResource


sealed class BottomBarScreen(
    val route: String,
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,




) {

    /** Novinky */
    object News : BottomBarScreen(
        route = "news",
        title = R.string.news,
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Default.DateRange,
        hasNews = false,
        badgeCount = null
    )

    /** AI Agent (chat) */
    object AiAgent : BottomBarScreen(
        route = "ai_agent",
        title = R.string.ai_agent,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Default.AutoAwesome,
        hasNews = false,
        badgeCount = null
    )

    /** Hlášení */
    object Reports : BottomBarScreen(
        route = "reports",
        title = R.string.reports,
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Default.Build,
        hasNews = true,
        badgeCount = null
    )
    object Maps : BottomBarScreen(
        route = "maps",
        title = R.string.maps,
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Default.LocationOn,
        hasNews = false,
        badgeCount = null
    )


}

