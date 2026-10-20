package com.duoc.proyectoaldemar.ui.nuevamuestra

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddSampleViewModel : ViewModel() {
    var centroCultivo by mutableStateOf("")
        private set

    var cantidadSemillas by mutableStateOf("")
        private set

    var observaciones by mutableStateOf("")
        private set

    fun onCentroCultivoChanged(value: String) {
        centroCultivo = value
    }

    fun onCantidadSemillasChanged(value: String) {
        cantidadSemillas = value
    }

    fun onObservacionesChanged(value: String) {
        observaciones = value
    }

    fun guardarMuestra(onSuccess: () -> Unit) {
        // Lógica para guardar la muestra
        onSuccess()
    }
}
