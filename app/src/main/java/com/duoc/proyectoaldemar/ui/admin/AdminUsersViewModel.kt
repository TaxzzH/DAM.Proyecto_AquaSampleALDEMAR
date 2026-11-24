package com.duoc.proyectoaldemar.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.data.InsforgeProvider
import com.duoc.proyectoaldemar.demo.DemoUsuarios
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Sesion
import dev.insforge.database.database
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

data class UsuarioAdminItem(
    val id: String,
    val email: String,
    val nombre: String,
    val rol: Rol
)

class AdminUsersViewModel : ViewModel() {
    var usuarios by mutableStateOf<List<UsuarioAdminItem>>(emptyList())
        private set
    var cargando by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set
    var filtroTexto by mutableStateOf("")

    val usuariosFiltrados: List<UsuarioAdminItem>
        get() {
            if (filtroTexto.isBlank()) return usuarios
            val query = filtroTexto.trim().lowercase()
            return usuarios.filter {
                it.nombre.lowercase().contains(query) ||
                it.email.lowercase().contains(query) ||
                it.rol.etiqueta.lowercase().contains(query)
            }
        }

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        cargando = true
        mensaje = null

        // Si la sesión activa es de un usuario demo
        if (Sesion.usuario?.codigo?.startsWith("IF-") != true) {
            usuarios = DemoUsuarios.usuarios.map {
                UsuarioAdminItem(
                    id = it.codigo,
                    email = "${it.codigo.lowercase()}@duocuc.cl",
                    nombre = it.nombre,
                    rol = it.rol
                )
            }
            cargando = false
            return
        }

        viewModelScope.launch {
            try {
                val rawResult = InsforgeProvider.client.database.from("usuarios")
                    .select("*")
                    .order("fecha_creacion", false)
                    .executeRaw()

                val lista = rawResult.mapNotNull { elemento ->
                    val obj = elemento as? JsonObject ?: return@mapNotNull null
                    val id = (obj["id"] as? JsonPrimitive)?.content ?: ""
                    val email = (obj["email"] as? JsonPrimitive)?.content ?: ""
                    val nombre = (obj["nombre"] as? JsonPrimitive)?.content ?: email.substringBefore("@")
                    val rolTexto = (obj["rol"] as? JsonPrimitive)?.content
                    UsuarioAdminItem(
                        id = id,
                        email = email,
                        nombre = nombre,
                        rol = Rol.desdeString(rolTexto)
                    )
                }
                usuarios = lista
            } catch (e: Exception) {
                mensaje = "Error al cargar usuarios: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }

    fun cambiarRol(usuario: UsuarioAdminItem, nuevoRol: Rol) {
        cargando = true

        // Si es usuario demo
        if (Sesion.usuario?.codigo?.startsWith("IF-") != true) {
            DemoUsuarios.actualizarRol(usuario.id, nuevoRol)
            usuarios = usuarios.map {
                if (it.id == usuario.id) it.copy(rol = nuevoRol) else it
            }
            cargando = false
            mensaje = "Rol de ${usuario.nombre} actualizado a ${nuevoRol.etiqueta} (Demo)"
            return
        }

        viewModelScope.launch {
            try {
                val datosActualizar = buildJsonObject {
                    put("rol", nuevoRol.name)
                }

                InsforgeProvider.client.database.from("usuarios")
                    .eq("id", usuario.id)
                    .update(datosActualizar)
                    .execute<JsonObject>()

                // Actualizar estado local en memoria
                usuarios = usuarios.map {
                    if (it.id == usuario.id) it.copy(rol = nuevoRol) else it
                }
                mensaje = "Rol de ${usuario.nombre} actualizado a ${nuevoRol.etiqueta}"
            } catch (e: Exception) {
                mensaje = "Error al actualizar rol: ${e.localizedMessage}"
            } finally {
                cargando = false
            }
        }
    }
}
