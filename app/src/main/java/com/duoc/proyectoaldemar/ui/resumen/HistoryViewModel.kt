package com.duoc.proyectoaldemar.ui.resumen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class SampleItem(
    val id: String,
    val centro: String,
    val cantidad: Int,
    val fecha: String
)

class HistoryViewModel : ViewModel() {
    val muestras = mutableStateListOf(
        SampleItem("M-101", "Seno Reloncaví Módulo 1", 1800, "2025-03-28"),
        SampleItem("M-102", "Seno Reloncaví Módulo 4", 2500, "2025-03-29"),
        SampleItem("M-103", "Calbuco Línea B", 3100, "2025-03-30")
    )
}
