package com.duoc.proyectoaldemar.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.ui.admin.AdminUsersScreen
import com.duoc.proyectoaldemar.ui.detalle.DetalleScreen
import com.duoc.proyectoaldemar.ui.inicio.HomeScreen
import com.duoc.proyectoaldemar.ui.login.LoginScreen
import com.duoc.proyectoaldemar.ui.navigation.BottomNavItem
import com.duoc.proyectoaldemar.ui.navigation.Screen
import com.duoc.proyectoaldemar.ui.nuevamuestra.AddSampleScreen
import com.duoc.proyectoaldemar.ui.resumen.HistoryScreen

/** Rutas principales con navegación unificada y barra inferior (NavigationBar). */
@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val argId = listOf(navArgument("id") { type = NavType.LongType })

    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val usuario = Sesion.usuario
    val showBottomBar = currentRoute in listOf(Screen.Home.route, Screen.History.route) && usuario?.rol != Rol.EN_ESPERA

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.History
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    nav.navigate(item.route) {
                                        popUpTo(nav.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(text = item.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(onIngreso = {
                    nav.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNueva = { nav.navigate("form/0") },
                    onDetalle = { nav.navigate("detalle/$it") },
                    onResumen = { nav.navigate(Screen.History.route) },
                    onGestionUsuarios = { nav.navigate(Screen.AdminUsers.route) },
                    onSalir = {
                        Sesion.usuario = null
                        nav.navigate(Screen.Login.route) { popUpTo(0) }
                    }
                )
            }
            composable(Screen.AdminUsers.route) {
                AdminUsersScreen(onVolver = { nav.popBackStack() })
            }
            composable("form/{id}", argId) {
                AddSampleScreen(onVolver = { nav.popBackStack() })
            }
            composable("detalle/{id}", argId) {
                DetalleScreen(onVolver = { nav.popBackStack() }, onEditar = { nav.navigate("form/$it") })
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    onVolver = { nav.popBackStack() },
                    onDetalle = { nav.navigate("detalle/$it") }
                )
            }
        }
    }
}
