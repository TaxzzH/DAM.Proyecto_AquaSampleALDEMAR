package com.duoc.proyectoaldemar.ui.nuevamuestra

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.duoc.proyectoaldemar.model.DatosDemo
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.Campo
import com.duoc.proyectoaldemar.ui.componentes.Selector
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme
import com.duoc.proyectoaldemar.util.Fotos
import com.duoc.proyectoaldemar.viewmodel.MuestraFormViewModel
import java.io.File

@Composable
fun AddSampleScreen(
    onVolver: () -> Unit = {},
    onSampleAdded: (() -> Unit)? = null,
    vm: MuestraFormViewModel = viewModel()
) {
    val handleVolver = onSampleAdded ?: onVolver
    val f = vm.f
    val e = vm.errores
    val context = LocalContext.current

    LaunchedEffect(vm.guardado) {
        if (vm.guardado) handleVolver()
    }

    var pendiente by rememberSaveable { mutableStateOf<String?>(null) }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) vm.editar(f.copy(fotoPath = pendiente))
    }
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) vm.editar(f.copy(fotoPath = Fotos.copiarDesdeGaleria(context, uri).absolutePath))
    }

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (f.id == 0L) "Nueva Muestra de Choritos" else "Editar Muestra #${f.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (f.estado == Estado.OBSERVADO) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "⚠️ Muestra Observada por Supervisor:",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = f.comentarioSupervisor,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Seccion 1: Ubicacion en Centro
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("1. Ubicación del Conteo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    Selector("Centro de Cultivo", f.centro, DatosDemo.centros, e["centro"], icono = Icons.Default.LocationOn) { vm.editar(f.copy(centro = it)) }
                    Selector("Tren", f.tren, DatosDemo.trenes, e["tren"]) { vm.editar(f.copy(tren = it)) }
                    Selector("Línea de Cultivo", f.linea, DatosDemo.lineas, e["linea"]) { vm.editar(f.copy(linea = it)) }
                }
            }

            // Seccion 2: Conteo y Medida
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Numbers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("2. Mediciones de Terreno", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    Campo("Tramo Medido (cm)", f.tramo, e["tramo"], teclado = KeyboardType.Number, icono = Icons.Default.Straighten) { vm.editar(f.copy(tramo = it)) }
                    Campo("Conteo de Individuos", f.conteo, e["conteo"], teclado = KeyboardType.Number, icono = Icons.Default.Numbers) { vm.editar(f.copy(conteo = it)) }

                    Text("Botones Rápidos de Conteo:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { vm.sumarConteo(-1) }, shape = RoundedCornerShape(10.dp)) { Text("-1") }
                        OutlinedButton(onClick = { vm.sumarConteo(1) }, shape = RoundedCornerShape(10.dp)) { Text("+1") }
                        OutlinedButton(onClick = { vm.sumarConteo(5) }, shape = RoundedCornerShape(10.dp)) { Text("+5") }
                        OutlinedButton(onClick = { vm.sumarConteo(10) }, shape = RoundedCornerShape(10.dp)) { Text("+10") }
                    }

                    Campo("Calibre Promedio mm (opcional)", f.calibre, e["calibre"], teclado = KeyboardType.Decimal) { vm.editar(f.copy(calibre = it)) }
                    Campo("Observaciones de Terreno", f.observaciones, e["observaciones"]) { vm.editar(f.copy(observaciones = it)) }
                }
            }

            // Seccion 3: Evidencia Fotografica
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("3. Fotografía de Respaldo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                val archivo = Fotos.nuevoArchivo(context)
                                pendiente = archivo.absolutePath
                                camara.launch(Fotos.uriPara(context, archivo))
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cámara")
                        }

                        OutlinedButton(
                            onClick = {
                                galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galería")
                        }
                    }

                    f.fotoPath?.let { fotoPath ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = File(fotoPath),
                                contentDescription = "Foto de la muestra",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { vm.editar(f.copy(fotoPath = null)) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Surface(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), shape = RoundedCornerShape(20.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Foto", tint = MaterialTheme.colorScheme.error, modifier = Modifier.padding(6.dp))
                                }
                            }
                        }
                    }

                    e["foto"]?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Acciones Finales
            AquaTrazButton(
                text = "Enviar a Revisión",
                icono = Icons.AutoMirrored.Filled.Send,
                onClick = { vm.guardar(enviar = true) }
            )

            OutlinedButton(
                onClick = { vm.guardar(enviar = false) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Guardar como Borrador", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = handleVolver,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddSampleScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        AddSampleScreen()
    }
}
