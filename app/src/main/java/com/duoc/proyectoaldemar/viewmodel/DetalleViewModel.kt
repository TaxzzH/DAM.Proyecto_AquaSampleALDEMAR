package com.duoc.proyectoaldemar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.AquaSampleApp
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.util.Validador
import kotlinx.coroutines.launch

/** Detalle de muestra y acciones de revisión del supervisor. */
class DetalleViewModel(app: Application, handle: SavedStateHandle) : AndroidViewModel(app) {
    private val repo = (app as AquaSampleApp).repo

    var muestra by mutableStateOf<Muestra?>(null)
        private set
    var errorComentario by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch { repo.observar(handle.get<Long>("id")!!).collect { muestra = it } }
    }

    fun validar() = cambiarEstado(Estado.VALIDADO, "")

    /** Devuelve false si el comentario del supervisor no cumple con el mínimo de caracteres. */
    fun observar(comentario: String): Boolean {
        errorComentario = !Validador.comentarioValido(comentario)
        if (!errorComentario) cambiarEstado(Estado.OBSERVADO, comentario.trim())
        return !errorComentario
    }

    private fun cambiarEstado(estado: Estado, comentario: String) {
        val m = muestra ?: return
        viewModelScope.launch { repo.guardar(m.copy(estado = estado, comentarioSupervisor = comentario)) }
    }

    fun eliminar(alTerminar: () -> Unit) {
        val m = muestra ?: return
        viewModelScope.launch { repo.eliminar(m); alTerminar() }
    }
}