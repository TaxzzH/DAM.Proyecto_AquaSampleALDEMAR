package com.duoc.proyectoaldemar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.data.InsforgeProvider
import com.duoc.proyectoaldemar.demo.DemoUsuarios
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.model.Usuario
import dev.insforge.auth.auth
import dev.insforge.database.database
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

class AuthViewModel : ViewModel() {
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    // Permite alternar a modo demo si no hay conexión o para pruebas locales
    var esModoDemo by mutableStateOf(false)

    /**
     * Extrae el rol asignado al usuario desde los campos 'profile' o 'metadata' de InsForge como respaldo.
     */
    private fun extraerRolDeMetadatos(profile: Map<String, String?>?, metadata: Map<String, String?>?): Rol {
        val rolString = profile?.get("rol")
            ?: profile?.get("role")
            ?: metadata?.get("rol")
            ?: metadata?.get("role")

        return Rol.desdeString(rolString)
    }

    /**
     * Inicia sesión usando InsForge (por defecto) o Modo Demo según 'esModoDemo'.
     */
    fun ingresar(onSuccess: () -> Unit) {
        val entrada = correo.trim()
        if (entrada.isBlank() || clave.isBlank()) {
            error = "Por favor ingresa ${if (esModoDemo) "código" else "correo"} y clave"
            return
        }

        if (esModoDemo) {
            val usuarioDemo = DemoUsuarios.autenticar(entrada, clave)
            if (usuarioDemo != null) {
                Sesion.usuario = usuarioDemo
                error = null
                onSuccess()
            } else {
                error = "Usuario o clave demo incorrectos (ej. ADM-01, OP-01, SUP-01, ESP-01 / 1234)"
            }
            return
        }

        cargando = true
        error = null

        viewModelScope.launch {
            try {
                val respuesta = InsforgeProvider.client.auth.signIn(
                    email = entrada,
                    password = clave
                )

                // 1. Intentamos leer el rol desde la tabla 'public.usuarios'
                var rolAsignado: Rol? = null
                try {
                    val rawResult = InsforgeProvider.client.database.from("usuarios")
                        .eq("id", respuesta.user.id)
                        .executeRaw()

                    val primerElemento = rawResult.firstOrNull() as? JsonObject
                    val rolTexto = (primerElemento?.get("rol") as? JsonPrimitive)?.content
                    if (!rolTexto.isNullOrBlank()) {
                        rolAsignado = Rol.desdeString(rolTexto)
                    }
                } catch (_: Exception) {
                    // Si no se pudo consultar la tabla, usaremos los metadatos de autenticación
                }

                // 2. Si no se obtuvo de la tabla, extraerlo de profile/metadata
                if (rolAsignado == null) {
                    rolAsignado = extraerRolDeMetadatos(respuesta.user.profile, respuesta.user.metadata)
                }

                val nombreMostrar = respuesta.user.name?.takeIf { it.isNotBlank() }
                    ?: entrada.substringBefore("@").replaceFirstChar { it.uppercase() }

                Sesion.usuario = Usuario(
                    codigo = "IF-${entrada.take(6).uppercase()}",
                    nombre = nombreMostrar,
                    clave = "",
                    rol = rolAsignado
                )
                onSuccess()
            } catch (e: Exception) {
                error = e.localizedMessage ?: "No se pudo iniciar sesión en InsForge."
            } finally {
                cargando = false
            }
        }
    }
}
