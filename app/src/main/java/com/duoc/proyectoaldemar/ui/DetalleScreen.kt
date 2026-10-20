package com.duoc.proyectoaldemar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.util.Reglas
import com.duoc.proyectoaldemar.viewmodel.DetalleViewModel
import java.io.File
import java.util.Locale

@Composable
fun DetalleScreen(onVolver: () -> Unit, onEditar: (Long) -> Unit, vm: DetalleViewModel = viewModel()) {
    val m = vm.muestra
    val usuario = Sesion.usuario!!
    var comentario by remember { mutableStateOf("") }

    Column(
        Modifier.systemBarsPadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(onClick = onVolver) { Text("Volver") }
        if (m == null) { Text("Cargando..."); return }

        Text("Muestra #${m.id} · Estado: ${m.estado.etiqueta}", style = MaterialTheme.typography.titleLarge)
        Text("${m.centro} · ${m.tren} · ${m.linea}")
        Text("${m.fechaHora.fecha()} · Operador: ${m.operador}")
        Text("Tramo: ${m.tramoCm} cm · Conteo: ${m.conteo} · Densidad: ${"%.1f".format(Locale.US, Reglas.densidad(m.conteo, m.tramoCm))} ind/m")
        m.calibreMm?.let { Text("Calibre: $it mm") }
        if (m.observaciones.isNotBlank()) Text("Observaciones: ${m.observaciones}")
        if (m.comentarioSupervisor.isNotBlank()) Text("Comentario Supervisor: ${m.comentarioSupervisor}", color = MaterialTheme.colorScheme.error)
        m.fotoPath?.let { AsyncImage(File(it), "Foto de la muestra", Modifier.fillMaxWidth().height(220.dp), contentScale = ContentScale.Crop) }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (Reglas.puedeEditar(usuario, m)) Button(onClick = { onEditar(m.id) }) { Text("Editar") }
            if (Reglas.puedeEliminar(usuario, m)) OutlinedButton(onClick = { vm.eliminar(onVolver) }) { Text("Eliminar") }
        }

        if (Reglas.puedeRevisar(usuario, m)) {
            Campo("Comentario de revisión (obligatorio al observar)", comentario, if (vm.errorComentario) "Explica la observación (mínimo 5 caracteres)" else null) { comentario = it }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.validar() }) { Text("Validar") }
                OutlinedButton(onClick = { vm.observar(comentario) }) { Text("Observar") }
            }
        }
    }
}