package com.duoc.proyectoaldemar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.AquaSampleApp
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.util.Reglas
import kotlinx.coroutines.launch

/** Indicadores de resumen y sincronización con la API remota. */
class ResumenViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as AquaSampleApp).repo

    var lista by mutableStateOf(emptyList<Muestra>())
        private set
    var cargando by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf("")
        private set

    val densidadPromedio get() = lista.map { Reglas.densidad(it.conteo, it.tramoCm) }.average().takeIf { !it.isNaN() } ?: 0.0

    init {
        viewModelScope.launch {
            repo.todas().collect { todas ->
                val u = Sesion.usuario
                lista = if (u != null) {
                    todas.filter { Reglas.esVisible(u, it) }
                } else {
                    emptyList()
                }
            }
        }
    }

    /** Maneja la sincronización de muestras validadas hacia la API. */
    fun sincronizar() {
        if (Sesion.usuario == null) {
            mensaje = "Sesión no válida. Inicia sesión nuevamente."
            return
        }
        viewModelScope.launch {
            cargando = true
            mensaje = try {
                "Sincronizadas: ${repo.sincronizar()}"
            } catch (_: Exception) {
                "No se pudo sincronizar. Revisa tu conexión."
            }
            cargando = false
        }
    }
}
