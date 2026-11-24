package com.duoc.proyectoaldemar.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField

@Composable
fun AdminUsersScreen(
    onVolver: () -> Unit = {},
    vm: AdminUsersViewModel = viewModel()
) {
    var usuarioSeleccionado by remember { mutableStateOf<UsuarioAdminItem?>(null) }
    var rolElegido by remember { mutableStateOf<Rol?>(null) }

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(12.dp))

        // Cabecera de la pantalla con botón de regreso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gestión de Usuarios",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Asignación de roles y permisos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
            TextButton(onClick = onVolver) {
                Text("← Volver")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mensaje de feedback si existe
        vm.mensaje?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Buscador de usuarios
        AquaTrazTextField(
            value = vm.filtroTexto,
            onValueChange = { vm.filtroTexto = it },
            label = "Buscar Usuario",
            placeholder = "Filtrar por nombre, correo o rol..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Usuarios Registrados (${vm.usuariosFiltrados.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { vm.cargarUsuarios() }, enabled = !vm.cargando) {
                Text("Actualizar")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (vm.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (vm.usuariosFiltrados.isEmpty()) {
            Text(
                text = "No se encontraron usuarios.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.usuariosFiltrados, key = { it.id }) { u ->
                    TarjetaUsuarioAdmin(
                        usuario = u,
                        onCambiarRol = {
                            usuarioSeleccionado = u
                            rolElegido = u.rol
                        }
                    )
                }
            }
        }
    }

    // Diálogo para seleccionar y cambiar el rol
    if (usuarioSeleccionado != null) {
        val targetUser = usuarioSeleccionado!!
        AlertDialog(
            onDismissRequest = { usuarioSeleccionado = null },
            title = {
                Text("Asignar Rol a ${targetUser.nombre}")
            },
            text = {
                Column {
                    Text(
                        text = "Correo: ${targetUser.email}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Selecciona el nuevo rol:", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Rol.entries.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { rolElegido = r }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = (rolElegido == r),
                                onClick = { rolElegido = r }
                            )
                            Text(
                                text = r.etiqueta,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                AquaTrazButton(
                    text = "Guardar Rol",
                    onClick = {
                        rolElegido?.let { nuevoRol ->
                            vm.cambiarRol(targetUser, nuevoRol)
                        }
                        usuarioSeleccionado = null
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { usuarioSeleccionado = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaUsuarioAdmin(
    usuario: UsuarioAdminItem,
    onCambiarRol: () -> Unit
) {
    val colorBadge = when (usuario.rol) {
        Rol.ADMINISTRADOR_ACADEMICO -> Color(0xFF6A1B9A) // Púrpura
        Rol.SUPERVISOR -> Color(0xFF0077B6)             // Azul corporativo
        Rol.ANALISTA -> Color(0xFF00897B)               // Verde azulado
        Rol.OPERADOR -> Color(0xFF0288D1)               // Celeste
        Rol.EN_ESPERA -> Color(0xFFE65100)              // Naranja advertencia
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuario.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(colorBadge.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = usuario.rol.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorBadge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TextButton(onClick = onCambiarRol) {
                Text("Cambiar Rol")
            }
        }
    }
}
