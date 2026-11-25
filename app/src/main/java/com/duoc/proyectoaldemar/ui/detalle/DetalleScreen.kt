package com.duoc.proyectoaldemar.ui.detalle

import android.graphics.BitmapFactory
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.Campo
import com.duoc.proyectoaldemar.ui.componentes.fecha
import com.duoc.proyectoaldemar.util.Reglas
import com.duoc.proyectoaldemar.viewmodel.DetalleViewModel
import java.io.File
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun DetalleScreen(
    onVolver: () -> Unit = {},
    onEditar: (Long) -> Unit = {},
    vm: DetalleViewModel = viewModel()
) {
    val m = vm.muestra
    val marcas = vm.marcas
    val usuario = Sesion.usuario
    var comentario by remember { mutableStateOf("") }

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(12.dp))

        if (m == null) {
            Text("Cargando información de muestra...", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onVolver) { Text("Volver") }
            return@AquaTrazScreen
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Muestra #${m.id} · Estado: ${m.estado.etiqueta}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Ubicación: ${m.centro} · ${m.tren} · ${m.linea}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Fecha: ${m.fechaHora.fecha()} · Operador: ${m.operador}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Tramo: ${m.tramoCm} cm · Conteo: ${m.conteo} ind (Marcas: ${marcas.size}) · Densidad: ${"%.1f".format(Locale.US, Reglas.densidad(m.conteo, m.tramoCm))} ind/m",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            m.calibreMm?.let {
                Text(
                    text = "Calibre: $it mm",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (m.observaciones.isNotBlank()) {
                Text(
                    text = "Observaciones: ${m.observaciones}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (m.comentarioSupervisor.isNotBlank()) {
                Text(
                    text = "Comentario Supervisor: ${m.comentarioSupervisor}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            m.fotoPath?.let { fotoPath ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Fotografía de Muestra (${marcas.size} marcas registradas):",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )

                    val bitmap = remember(fotoPath) {
                        val file = File(fotoPath)
                        if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
                    }

                    if (bitmap != null) {
                        var containerSize by remember { mutableStateOf(IntSize.Zero) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black)
                                .onGloballyPositioned { containerSize = it.size }
                        ) {
                            if (containerSize.width > 0 && containerSize.height > 0) {
                                val W_c = containerSize.width.toFloat()
                                val H_c = containerSize.height.toFloat()
                                val W_i = bitmap.width.toFloat()
                                val H_i = bitmap.height.toFloat()

                                val scaleFit = kotlin.math.min(W_c / W_i, H_c / H_i)
                                val W_draw = W_i * scaleFit
                                val H_draw = H_i * scaleFit
                                val offsetX_draw = (W_c - W_draw) / 2f
                                val offsetY_draw = (H_c - H_draw) / 2f

                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawImage(
                                        image = bitmap.asImageBitmap(),
                                        dstOffset = IntOffset(offsetX_draw.roundToInt(), offsetY_draw.roundToInt()),
                                        dstSize = IntSize(W_draw.roundToInt(), H_draw.roundToInt())
                                    )

                                    val radiusPx = 14.dp.toPx()
                                    marcas.forEach { marca ->
                                        val x_draw = offsetX_draw + (marca.xNorm * W_draw).toFloat()
                                        val y_draw = offsetY_draw + (marca.yNorm * H_draw).toFloat()

                                        drawCircle(
                                            color = Color.Black.copy(alpha = 0.5f),
                                            radius = radiusPx + 2.dp.toPx(),
                                            center = Offset(x_draw, y_draw)
                                        )

                                        drawCircle(
                                            color = Color.White,
                                            radius = radiusPx + 1.dp.toPx(),
                                            center = Offset(x_draw, y_draw)
                                        )

                                        drawCircle(
                                            color = Color(0xFF0077B6),
                                            radius = radiusPx,
                                            center = Offset(x_draw, y_draw)
                                        )

                                        drawCircle(
                                            color = Color.White,
                                            radius = 2.dp.toPx(),
                                            center = Offset(x_draw, y_draw)
                                        )

                                        drawContext.canvas.nativeCanvas.apply {
                                            val text = marca.orden.toString()
                                            val paint = Paint().apply {
                                                color = android.graphics.Color.WHITE
                                                textSize = 11.sp.toPx()
                                                isFakeBoldText = true
                                                textAlign = Paint.Align.CENTER
                                            }
                                            val textY = y_draw - ((paint.descent() + paint.ascent()) / 2)
                                            drawText(text, x_draw, textY, paint)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        AsyncImage(
                            model = File(fotoPath),
                            contentDescription = "Foto de la muestra",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Opciones del operador (Editar / Eliminar)
            if (usuario != null && Reglas.puedeEditar(usuario, m)) {
                AquaTrazButton(
                    text = "Editar Muestra",
                    onClick = { onEditar(m.id) }
                )
            }

            if (usuario != null && Reglas.puedeEliminar(usuario, m)) {
                OutlinedButton(
                    onClick = { vm.eliminar(onVolver) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Eliminar Muestra", color = MaterialTheme.colorScheme.error)
                }
            }

            // Opciones del supervisor (Validar / Observar)
            if (usuario != null && Reglas.puedeRevisar(usuario, m)) {
                Spacer(modifier = Modifier.height(8.dp))
                Campo(
                    etiqueta = "Comentario de revisión (obligatorio al observar)",
                    valor = comentario,
                    error = if (vm.errorComentario) "Explica la observación (mínimo 5 caracteres)" else null
                ) { comentario = it }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { vm.validar() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Validar")
                    }

                    OutlinedButton(
                        onClick = { vm.observar(comentario) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Observar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Volver")
            }
        }
    }
}
