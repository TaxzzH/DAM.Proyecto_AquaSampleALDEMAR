package com.duoc.proyectoaldemar.ui.inicio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var totalMuestras by mutableIntStateOf(12)
        private set

    var centroActivo by mutableStateOf("Centro Seno Reloncaví")
        private set

    var ultimoRegistro by mutableStateOf("Muestra #102 - Mytilus edulis (Chorito)")
        private set
}
