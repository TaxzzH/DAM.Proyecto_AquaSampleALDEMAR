package com.duoc.proyectoaldemar.ui.nuevamuestra

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.duoc.proyectoaldemar.ui.componentes.Campo
import com.duoc.proyectoaldemar.ui.componentes.Selector
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
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

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (f.id == 0L) "Nueva Muestra de Choritos" else "Editar Muestra #${f.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (f.estado == Estado.OBSERVADO) {
                Text(
                    text = "Observación del supervisor: ${f.comentarioSupervisor}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Selector("Centro", f.centro, DatosDemo.centros, e["centro"]) { vm.editar(f.copy(centro = it)) }
            Selector("Tren", f.tren, DatosDemo.trenes, e["tren"]) { vm.editar(f.copy(tren = it)) }
            Selector("Línea", f.linea, DatosDemo.lineas, e["linea"]) { vm.editar(f.copy(linea = it)) }

            Campo("Tramo (cm)", f.tramo, e["tramo"], teclado = KeyboardType.Number) { vm.editar(f.copy(tramo = it)) }
            Campo("Conteo de individuos", f.conteo, e["conteo"], teclado = KeyboardType.Number) { vm.editar(f.copy(conteo = it)) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { vm.sumarConteo(1) }) { Text("+1") }
                OutlinedButton(onClick = { vm.sumarConteo(10) }) { Text("+10") }
            }

            Campo("Calibre mm (opcional)", f.calibre, e["calibre"], teclado = KeyboardType.Decimal) { vm.editar(f.copy(calibre = it)) }
            Campo("Observaciones", f.observaciones, e["observaciones"]) { vm.editar(f.copy(observaciones = it)) }

            Text(
                text = "Fotografía de Muestra",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    val archivo = Fotos.nuevoArchivo(context)
                    pendiente = archivo.absolutePath
                    camara.launch(Fotos.uriPara(context, archivo))
                }) { Text("Tomar Foto (Cámara)") }

                OutlinedButton(onClick = {
                    galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("Galería") }
            }

            f.fotoPath?.let {
                AsyncImage(
                    model = File(it),
                    contentDescription = "Foto de la muestra",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            e["foto"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(modifier = Modifier.height(8.dp))

            AquaTrazButton(
                text = "Enviar a Revisión",
                onClick = { vm.guardar(enviar = true) }
            )

            OutlinedButton(
                onClick = { vm.guardar(enviar = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Borrador")
            }

            OutlinedButton(
                onClick = handleVolver,
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
