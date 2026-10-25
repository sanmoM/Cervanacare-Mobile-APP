package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.UserRole
import com.example.ui.theme.*

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun CervannaBottomNav(
    currentRole: UserRole,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = if (currentRole == UserRole.CUSTOMER) {
        listOf(
            NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_customer_home"),
            NavItem("Explore", Icons.Filled.Search, Icons.Outlined.Search, "nav_customer_explore"),
            NavItem("Bookings", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "nav_customer_bookings"),
            NavItem("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "nav_customer_favorites"),
            NavItem("Account", Icons.Filled.Person, Icons.Outlined.Person, "nav_customer_account")
        )
    } else {
        listOf(
            NavItem("Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "nav_specialist_dashboard"),
            NavItem("Bookings", Icons.Filled.EventNote, Icons.Outlined.EventNote, "nav_specialist_bookings"),
            NavItem("Availability", Icons.Filled.Schedule, Icons.Outlined.Schedule, "nav_specialist_availability"),
            NavItem("Earnings", Icons.Filled.Payments, Icons.Outlined.Payments, "nav_specialist_earnings"),
            NavItem("Profile", Icons.Filled.Badge, Icons.Outlined.Badge, "nav_specialist_profile")
        )
    }

    NavigationBar(
        containerColor = CervannaSurface,
        tonalElevation = 8.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedTabIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CervannaTealPrimary,
                    selectedTextColor = CervannaTealPrimary,
                    indicatorColor = CervannaTealContainer,
                    unselectedIconColor = CervannaTextSecondary,
                    unselectedTextColor = CervannaTextSecondary
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
