package com.duoc.proyectoaldemar.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onGestionUsuarios: () -> Unit = {},
    onNavigateToAddSample: (() -> Unit)? = null,
    onNavigateToHistory: (() -> Unit)? = null,
    vm: MuestrasViewModel = viewModel()
) {
    val handleNueva = onNavigateToAddSample ?: onNueva
    val handleResumen = onNavigateToHistory ?: onResumen
    val usuario = Sesion.usuario
    var menuOpcionesAbierto by remember { mutableStateOf(false) }

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de información de Usuario con menú desplegable de 3 puntos
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
                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = u.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rol: ${u.rol.etiqueta}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                    }

                    Box {
                        IconButton(onClick = { menuOpcionesAbierto = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menú de opciones",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = menuOpcionesAbierto,
                            onDismissRequest = { menuOpcionesAbierto = false }
                        ) {
                            if (u.rol != Rol.EN_ESPERA) {
                                DropdownMenuItem(
                                    text = { Text("Resumen de Muestras") },
                                    onClick = {
                                        menuOpcionesAbierto = false
                                        handleResumen()
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    menuOpcionesAbierto = false
                                    onSalir()
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Si el usuario está EN ESPERA, no tiene acceso a las funciones operativas
        if (usuario?.rol == Rol.EN_ESPERA) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Cuenta en Espera de Activación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tu cuenta se encuentra registrada en AquaTraz, pero actualmente no tienes un rol operativo asignado.\n\nPor favor, contacta a un Administrador Académico para que active tu cuenta y te asigne tu rol (Operador, Supervisor o Analista).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else {
            // Panel exclusivo para Administrador Académico
            if (usuario?.rol == Rol.ADMINISTRADOR_ACADEMICO) {
                AquaTrazButton(
                    text = "🛡️ Gestión de Roles de Usuarios",
                    onClick = onGestionUsuarios
                )
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
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        HomeScreen()
    }
}
