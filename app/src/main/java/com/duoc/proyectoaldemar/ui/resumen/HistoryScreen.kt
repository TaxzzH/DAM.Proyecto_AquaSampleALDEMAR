package com.duoc.proyectoaldemar.ui.resumen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.ui.componentes.TarjetaMuestra
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme
import com.duoc.proyectoaldemar.viewmodel.ResumenViewModel
import java.util.Locale

@Composable
fun HistoryScreen(
    onVolver: () -> Unit = {},
    onDetalle: (Long) -> Unit = {},
    vm: ResumenViewModel = viewModel()
) {
    val lista = vm.lista
    val porSincronizar = lista.count { it.estado == Estado.VALIDADO && !it.sincronizada }

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Resumen y Métricas ALDEMAR SpA",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjeta de Resumen Estadístico
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Total Muestras: ${lista.size}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Individuos Contados: ${lista.sumOf { it.conteo }}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Densidad Promedio: ${"%.1f".format(Locale.US, vm.densidadPromedio)} ind/m",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Desglose por Estado:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Estado.entries.forEach { e ->
                    Text(
                        text = "• ${e.etiqueta}: ${lista.count { it.estado == e }}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Validadas por Sincronizar: $porSincronizar",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (porSincronizar > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para sincronizar
        AquaTrazButton(
            text = if (vm.cargando) "Sincronizando..." else "Sincronizar a Servidor ($porSincronizar)",
            onClick = { vm.sincronizar() },
            enabled = !vm.cargando && porSincronizar > 0
        )

        if (vm.cargando) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (vm.mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = vm.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Historial de Muestras",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (lista.isEmpty()) {
            Text(
                text = "No hay muestras registradas.",
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
                items(lista, key = { it.id }) { m ->
                    TarjetaMuestra(m = m, onClick = { onDetalle(m.id) })
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AquaTrazButton(
            text = "Volver",
            onClick = onVolver
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        HistoryScreen()
    }
}
