package com.duoc.proyectoaldemar.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.duoc.proyectoaldemar.ui.inicio.HomeScreen
import com.duoc.proyectoaldemar.ui.login.LoginScreen
import com.duoc.proyectoaldemar.ui.nuevamuestra.AddSampleScreen
import com.duoc.proyectoaldemar.ui.resumen.HistoryScreen
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme

@Composable
fun AquaTrazNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.AddSample,
        BottomNavItem.History
    )

    // Ocultar la barra inferior en la pantalla de Login
    val showBottomBar = currentRoute != Screen.Login.route && currentRoute != null

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
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
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
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Pantalla Login
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Pantalla Inicio (Home)
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToAddSample = {
                        navController.navigate(Screen.AddSample.route)
                    },
                    onNavigateToHistory = {
                        navController.navigate(Screen.History.route)
                    }
                )
            }

            // 3. Pantalla Nueva Muestra (AddSample)
            composable(Screen.AddSample.route) {
                AddSampleScreen(
                    onSampleAdded = {
                        navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                )
            }

            // 4. Pantalla Resumen e Historial (History)
            composable(Screen.History.route) {
                HistoryScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AquaTrazNavGraphPreview() {
    EP2_ProyectoALDEMARTheme {
        AquaTrazNavGraph()
    }
}
