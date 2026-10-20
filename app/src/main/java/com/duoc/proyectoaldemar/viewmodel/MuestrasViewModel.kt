package com.duoc.proyectoaldemar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.AquaSampleApp
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.util.Reglas
import kotlinx.coroutines.launch

/** Historial: muestra las muestras visibles para el usuario con búsqueda y filtro. */
class MuestrasViewModel(app: Application) : AndroidViewModel(app) {
    private val usuario = Sesion.usuario!!
    private var visibles by mutableStateOf(emptyList<Muestra>())

    var texto by mutableStateOf("")
    var estado by mutableStateOf<Estado?>(null)

    val muestras get() = Reglas.filtrar(visibles, texto, estado)

    init {
        // Room emite una lista nueva automáticamente cada vez que cambia la base de datos
        viewModelScope.launch {
            (app as AquaSampleApp).repo.todas().collect { todas ->
                visibles = todas.filter { Reglas.esVisible(usuario, it) }
            }
        }
    }
}