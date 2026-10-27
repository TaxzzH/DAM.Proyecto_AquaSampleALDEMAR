package com.duoc.proyectoaldemar.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.ui.componentes.TarjetaMuestra
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme
import com.duoc.proyectoaldemar.viewmodel.MuestrasViewModel

@Composable
fun HomeScreen(
    onNueva: () -> Unit = {},
    onDetalle: (Long) -> Unit = {},
    onResumen: () -> Unit = {},
    onSalir: () -> Unit = {},
    onNavigateToAddSample: (() -> Unit)? = null,
    onNavigateToHistory: (() -> Unit)? = null,
    vm: MuestrasViewModel = viewModel()
) {
    val handleNueva = onNavigateToAddSample ?: onNueva
    val handleResumen = onNavigateToHistory ?: onResumen
    val usuario = Sesion.usuario

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de información de Usuario y Acciones de Cabecera
        usuario?.let { u ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = u.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rol: ${u.rol.etiqueta}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Row {
                        TextButton(onClick = handleResumen) {
                            Text("Resumen")
                        }
                        TextButton(onClick = onSalir) {
                            Text("Salir", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Búsqueda por filtro
        AquaTrazTextField(
            value = vm.texto,
            onValueChange = { vm.texto = it },
            label = "Buscar Muestra",
            placeholder = "Centro, tren, línea u operador..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Chips de filtro por estado
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = vm.estado == null,
                    onClick = { vm.estado = null },
                    label = { Text("Todos") }
                )
            }
            items(Estado.entries) { e ->
                FilterChip(
                    selected = vm.estado == e,
                    onClick = { vm.estado = e },
                    label = { Text(e.etiqueta) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Nueva Muestra sólo si es Operador
        if (usuario?.rol == Rol.OPERADOR) {
            AquaTrazButton(
                text = "+ Nueva Muestra",
                onClick = handleNueva
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "Muestras Registradas (${vm.muestras.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (vm.muestras.isEmpty()) {
            Text(
                text = "No hay muestras para mostrar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.muestras, key = { it.id }) { m ->
                    TarjetaMuestra(m = m, onClick = { onDetalle(m.id) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        HomeScreen()
    }
}
