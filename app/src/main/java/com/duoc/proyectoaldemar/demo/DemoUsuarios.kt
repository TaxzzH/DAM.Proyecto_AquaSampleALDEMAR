package com.duoc.proyectoaldemar.demo

import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Usuario

/**
 * Modo Demo: Usuarios y autenticación local para pruebas sin conexión.
 * Separado del flujo de autenticación principal (InsForge).
 */
object DemoUsuarios {
    val usuarios = mutableListOf(
        Usuario("ADM-01", "Admin Demo", "1234", Rol.ADMINISTRADOR_ACADEMICO),
        Usuario("OP-01", "Operador Uno (Demo)", "1234", Rol.OPERADOR),
        Usuario("OP-02", "Operador Dos (Demo)", "1234", Rol.OPERADOR),
        Usuario("SUP-01", "Supervisor Uno (Demo)", "1234", Rol.SUPERVISOR),
        Usuario("AN-01", "Analista Uno (Demo)", "1234", Rol.ANALISTA),
        Usuario("ESP-01", "Usuario En Espera (Demo)", "1234", Rol.EN_ESPERA)
    )

    fun autenticar(codigo: String, clave: String): Usuario? {
        return usuarios.find {
            it.codigo.equals(codigo.trim(), ignoreCase = true) && it.clave == clave
        }
    }

    fun actualizarRol(codigo: String, nuevoRol: Rol) {
        val index = usuarios.indexOfFirst { it.codigo == codigo }
        if (index != -1) {
            usuarios[index] = usuarios[index].copy(rol = nuevoRol)
        }
    }
}
