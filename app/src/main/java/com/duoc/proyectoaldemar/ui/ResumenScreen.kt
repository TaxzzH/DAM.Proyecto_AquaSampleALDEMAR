package com.duoc.proyectoaldemar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.viewmodel.ResumenViewModel
import java.util.Locale

@Composable
fun ResumenScreen(onVolver: () -> Unit, vm: ResumenViewModel = viewModel()) {
    val lista = vm.lista
    Column(Modifier.systemBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onVolver) { Text("Volver") }
        Text("Resumen y Métricas ALDEMAR SpA", style = MaterialTheme.typography.titleLarge)
        Text("Total Muestras: ${lista.size}")
        Text("Individuos Contados: ${lista.sumOf { it.conteo }}")
        Text("Densidad Promedio: ${"%.1f".format(Locale.US, vm.densidadPromedio)} ind/m")
        Estado.entries.forEach { e -> Text("${e.etiqueta}: ${lista.count { it.estado == e }}") }

        val porSincronizar = lista.count { it.estado == Estado.VALIDADO && !it.sincronizada }
        Text("Muestras Validadas por Sincronizar: $porSincronizar")
        Button(onClick = { vm.sincronizar() }, enabled = !vm.cargando && porSincronizar > 0) { Text("Sincronizar a Servidor") }
        if (vm.cargando) CircularProgressIndicator()
        if (vm.mensaje.isNotEmpty()) Text(vm.mensaje)
    }
}