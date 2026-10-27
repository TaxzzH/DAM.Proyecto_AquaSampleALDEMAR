package com.duoc.proyectoaldemar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.ui.detalle.DetalleScreen
import com.duoc.proyectoaldemar.ui.inicio.HomeScreen
import com.duoc.proyectoaldemar.ui.login.LoginScreen
import com.duoc.proyectoaldemar.ui.nuevamuestra.AddSampleScreen
import com.duoc.proyectoaldemar.ui.resumen.HistoryScreen

/** Rutas principales: form/0 = nueva muestra; form/5 = editar muestra #5 */
@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val argId = listOf(navArgument("id") { type = NavType.LongType })

    NavHost(nav, startDestination = "login") {
        composable("login") {
            LoginScreen(onIngreso = { nav.navigate("muestras") { popUpTo("login") { inclusive = true } } })
        }
        composable("muestras") {
            HomeScreen(
                onNueva = { nav.navigate("form/0") },
                onDetalle = { nav.navigate("detalle/$it") },
                onResumen = { nav.navigate("resumen") },
                onSalir = { Sesion.usuario = null; nav.navigate("login") { popUpTo(0) } }
            )
        }
        composable("form/{id}", argId) {
            AddSampleScreen(onVolver = { nav.popBackStack() })
        }
        composable("detalle/{id}", argId) {
            DetalleScreen(onVolver = { nav.popBackStack() }, onEditar = { nav.navigate("form/$it") })
        }
        composable("resumen") {
            HistoryScreen(
                onVolver = { nav.popBackStack() },
                onDetalle = { nav.navigate("detalle/$it") }
            )
        }
    }
}
