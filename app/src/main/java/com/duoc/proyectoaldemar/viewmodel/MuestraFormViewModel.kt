package com.duoc.proyectoaldemar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.AquaSampleApp
import com.duoc.proyectoaldemar.model.Formulario
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.util.Reglas
import com.duoc.proyectoaldemar.util.Validador
import kotlinx.coroutines.launch

/** Formulario de muestra. El id llega mediante SavedStateHandle (0 = nueva muestra). */
class MuestraFormViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val repo = (app as AquaSampleApp).repo

    var f by mutableStateOf(Formulario(operador = Sesion.usuario!!.codigo))
        private set
    var errores by mutableStateOf<Map<String, String>>(emptyMap())
        private set
    var guardado by mutableStateOf(false)
        private set

    init {
        val id = handle.get<Long>("id") ?: 0L
        if (id != 0L) viewModelScope.launch { repo.obtener(id)?.let { f = Formulario.desde(it) } }
    }

    fun editar(nuevo: Formulario) { f = nuevo }

    fun sumarConteo(delta: Int) {
        f = f.copy(conteo = ((f.conteo.toIntOrNull() ?: 0) + delta).coerceIn(0, 5000).toString())
    }

    /** enviar = false guarda borrador; enviar = true exige datos completos y fotografía. */
    fun guardar(enviar: Boolean) {
        errores = Validador.validar(f, enviar)
        if (errores.isNotEmpty()) return
        val estado = if (enviar) Reglas.estadoAlEnviar(f.estado) else f.estado
        viewModelScope.launch {
            repo.guardar(f.aMuestra(estado))
            guardado = true
        }
    }
}