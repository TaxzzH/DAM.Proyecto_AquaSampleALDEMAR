package com.duoc.proyectoaldemar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.viewmodel.MuestrasViewModel

@Composable
fun MuestrasScreen(
    onNueva: () -> Unit,
    onDetalle: (Long) -> Unit,
    onResumen: () -> Unit,
    onSalir: () -> Unit,
    vm: MuestrasViewModel = viewModel()
) {
    val usuario = Sesion.usuario!!
    Scaffold(floatingActionButton = {
        if (usuario.rol == Rol.OPERADOR) {
            ExtendedFloatingActionButton(onClick = onNueva) { Text("Nueva Muestra") }
        }
    }) { padding ->
        Column(
            Modifier.padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${usuario.nombre} (${usuario.rol.etiqueta})", style = MaterialTheme.typography.titleMedium)
                Row {
                    TextButton(onClick = onResumen) { Text("Resumen") }
                    TextButton(onClick = onSalir) { Text("Salir") }
                }
            }
            OutlinedTextField(
                value = vm.texto,
                onValueChange = { vm.texto = it },
                label = { Text("Buscar por centro, tren, línea u operador") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { FilterChip(vm.estado == null, { vm.estado = null }, { Text("Todos") }) }
                items(Estado.entries) { e -> FilterChip(vm.estado == e, { vm.estado = e }, { Text(e.etiqueta) }) }
            }
            if (vm.muestras.isEmpty()) Text("No hay muestras para mostrar.")
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.muestras, key = { it.id }) { m -> TarjetaMuestra(m) { onDetalle(m.id) } }
            }
        }
    }
}