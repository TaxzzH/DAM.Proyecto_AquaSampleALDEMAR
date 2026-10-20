package com.duoc.proyectoaldemar.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object AddSample : Screen("add_sample")
    object History : Screen("history")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        title = "Inicio",
        icon = Icons.Default.Home
    )

    object AddSample : BottomNavItem(
        route = Screen.AddSample.route,
        title = "Nueva Muestra",
        icon = Icons.Default.AddCircle
    )

    object History : BottomNavItem(
        route = Screen.History.route,
        title = "Historial",
        icon = Icons.Default.List
    )
}