package com.duoc.proyectoaldemar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.proyectoaldemar.model.DatosDemo
import com.duoc.proyectoaldemar.model.Sesion

class AuthViewModel : ViewModel() {
    var codigo by mutableStateOf("")
    var clave by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
        private set

    /** Devuelve true si las credenciales son correctas y registra al usuario en la sesión. */
    fun ingresar(): Boolean {
        val usuario = DatosDemo.usuarios.find { it.codigo == codigo.trim().uppercase() && it.clave == clave }
        Sesion.usuario = usuario
        error = if (usuario == null) "Código o clave incorrectos" else null
        return usuario != null
    }
}