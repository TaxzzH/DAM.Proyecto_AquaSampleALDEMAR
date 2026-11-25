package com.duoc.proyectoaldemar.ui.resumen

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.Selector
import com.duoc.proyectoaldemar.ui.componentes.TarjetaMuestra
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme
import com.duoc.proyectoaldemar.viewmodel.ConteoPorCentroItem
import com.duoc.proyectoaldemar.viewmodel.PuntoEvolucionTemporal
import com.duoc.proyectoaldemar.viewmodel.RangoFecha
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

        // 1. Tarjetas de Indicadores Principales (KPI Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0077B6)),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Muestras", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(
                        text = "${vm.totalMuestras}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00B4D8)),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Individuos", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(
                        text = "${vm.totalIndividuos}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF48CAE4)),
                modifier = Modifier.weight(1.1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Densidad Prom.", color = Color(0xFF1B1B1B).copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(
                        text = "%.1f".format(Locale.US, vm.densidadPromedio),
                        color = Color(0xFF1B1B1B),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("ind/m", color = Color(0xFF1B1B1B).copy(alpha = 0.7f), fontSize = 10.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Sección de Filtros Dinámicos
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = "Filtros de Análisis",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (vm.hayFiltrosActivos) {
                        OutlinedButton(onClick = { vm.limpiarFiltros() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar filtros",
                                modifier = Modifier.padding(end = 2.dp)
                            )
                            Text("Limpiar", fontSize = 11.sp)
                        }
                    }
                }

                // Filtro Centro
                Selector(
                    etiqueta = "Centro de cultivo",
                    valor = if (vm.centroSeleccionado == "Todos") "Todos los centros" else vm.centroSeleccionado,
                    opciones = vm.centrosDisponibles,
                    error = null,
                    onElegir = { vm.cambiarCentro(if (it == "Todos los centros") "Todos" else it) }
                )

                // Filtro Fecha
                Text(
                    text = "Rango de fechas:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RangoFecha.entries.forEach { rf ->
                        FilterChip(
                            selected = vm.rangoFechaSeleccionado == rf,
                            onClick = { vm.cambiarRangoFecha(rf) },
                            label = { Text(rf.etiqueta, fontSize = 12.sp) }
                        )
                    }
                }

                // Filtro Estado
                Text(
                    text = "Estado de revisión:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = vm.estadoSeleccionado == null,
                        onClick = { vm.cambiarEstado(null) },
                        label = { Text("Todos", fontSize = 12.sp) }
                    )
                    Estado.entries.forEach { e ->
                        FilterChip(
                            selected = vm.estadoSeleccionado == e,
                            onClick = { vm.cambiarEstado(e) },
                            label = { Text(e.etiqueta, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Gráfico A: Distribución de Muestras por Estado
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Distribución de Muestras por Estado",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                vm.distribucionPorEstado.forEach { item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("• ${item.estado.etiqueta}", style = MaterialTheme.typography.bodySmall)
                            Text("${item.cantidad} (${(item.porcentaje * 100).toInt()}%)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { item.porcentaje },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            color = when (item.estado) {
                                Estado.VALIDADO -> Color(0xFF2E7D32)
                                Estado.OBSERVADO -> Color(0xFFD32F2F)
                                Estado.CORREGIDO -> Color(0xFFED6C02)
                                Estado.PENDIENTE -> Color(0xFF0288D1)
                                Estado.BORRADOR -> Color.Gray
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Gráfico B: Evolución Temporal de Muestras
        EvolucionTemporalChart(puntos = vm.evolucionTemporal)

        Spacer(modifier = Modifier.height(12.dp))

        // 5. Gráfico C: Individuos Registrados por Centro
        IndividuosPorCentroChart(items = vm.individuosPorCentro)

        Spacer(modifier = Modifier.height(12.dp))

        // 6. Sección de Sincronización
        AquaTrazButton(
            text = if (vm.cargando) "Sincronizando..." else "Sincronizar a Servidor ($porSincronizar validadas)",
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Historial de Muestras (${lista.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (lista.isEmpty()) {
            Text(
                text = if (vm.hayFiltrosActivos) "No hay muestras que coincidan con los filtros seleccionados." else "No hay muestras registradas.",
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

@Composable
fun EvolucionTemporalChart(puntos: List<PuntoEvolucionTemporal>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Evolución Temporal de Muestras",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            if (puntos.isEmpty()) {
                Text(
                    text = "No hay registros temporales para el filtro actual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                val maxCant = puntos.maxOf { it.cantidad }.coerceAtLeast(1)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val W = size.width
                        val H = size.height
                        val paddingX = 24.dp.toPx()
                        val paddingBottom = 18.dp.toPx()
                        val chartHeight = H - paddingBottom

                        val numPuntos = puntos.size
                        val barWidth = ((W - 2 * paddingX) / numPuntos.toFloat() * 0.5f).coerceIn(12.dp.toPx(), 36.dp.toPx())
                        val stepX = (W - 2 * paddingX) / numPuntos.toFloat()

                        puntos.forEachIndexed { idx, p ->
                            val centerX = paddingX + idx * stepX + stepX / 2f
                            val barH = (p.cantidad.toFloat() / maxCant) * (chartHeight - 16.dp.toPx())
                            val topY = chartHeight - barH

                            drawRoundRect(
                                color = Color(0xFF0077B6),
                                topLeft = Offset(centerX - barWidth / 2f, topY),
                                size = Size(barWidth, barH),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )

                            drawContext.canvas.nativeCanvas.apply {
                                val paintX = Paint().apply {
                                    color = android.graphics.Color.GRAY
                                    textSize = 10.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                }
                                drawText(p.fechaTexto, centerX, H - 2.dp.toPx(), paintX)

                                val paintVal = Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    textSize = 11.sp.toPx()
                                    isFakeBoldText = true
                                    textAlign = Paint.Align.CENTER
                                }
                                drawText(p.cantidad.toString(), centerX, topY - 3.dp.toPx(), paintVal)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IndividuosPorCentroChart(items: List<ConteoPorCentroItem>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Individuos Registrados por Centro",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            if (items.isEmpty()) {
                Text(
                    text = "No hay registros por centro para el filtro actual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                val maxInd = items.maxOf { it.totalIndividuos }.coerceAtLeast(1)

                items.forEach { item ->
                    val pct = item.totalIndividuos.toFloat() / maxInd
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.centro,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${item.totalIndividuos} ind",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0077B6)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            progress = { pct },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Color(0xFF00B4D8),
                            trackColor = Color(0xFF00B4D8).copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        HistoryScreen()
    }
}
