package com.duoc.proyectoaldemar.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.util.Reglas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.fecha(): String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(this))

/** Campo de texto con mensaje de error debajo. */
@Composable
fun Campo(
    etiqueta: String,
    valor: String,
    error: String?,
    modifier: Modifier = Modifier.fillMaxWidth(),
    teclado: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) = OutlinedTextField(
    value = valor,
    onValueChange = onChange,
    label = { Text(etiqueta) },
    isError = error != null,
    supportingText = error?.let { { Text(it) } },
    keyboardOptions = KeyboardOptions(keyboardType = teclado),
    modifier = modifier
)

/** Lista desplegable para elegir una opción (Centro, Tren, Línea). */
@Composable
fun Selector(
    etiqueta: String,
    valor: String,
    opciones: List<String>,
    error: String?,
    onElegir: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    Column {
        OutlinedButton(onClick = { abierto = true }, modifier = Modifier.fillMaxWidth()) {
            Text(valor.ifEmpty { etiqueta })
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { onElegir(it); abierto = false }) }
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
    }
}

/** Tarjeta de una muestra en el historial. */
@Composable
fun TarjetaMuestra(m: Muestra, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(12.dp)) {
            Text("${m.centro} · ${m.tren} · ${m.linea}", style = MaterialTheme.typography.titleSmall)
            Text("${m.fechaHora.fecha()} · Operador: ${m.operador}")
            Text("Conteo: ${m.conteo} (${"%.1f".format(Locale.US, Reglas.densidad(m.conteo, m.tramoCm))} ind/m) · Estado: ${m.estado.etiqueta}")
        }
    }
}