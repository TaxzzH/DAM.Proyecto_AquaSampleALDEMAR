package com.duoc.proyectoaldemar.model

enum class Rol(val etiqueta: String) {
    OPERADOR("Operador"),
    SUPERVISOR("Supervisor"),
    ANALISTA("Analista"),
    ADMINISTRADOR_ACADEMICO("Administrador Académico"),
    EN_ESPERA("En espera");

    companion object {
        fun desdeString(valor: String?): Rol {
            return when (valor?.trim()?.uppercase()) {
                "ADMINISTRADOR_ACADEMICO", "ADMIN", "ADMINISTRADOR" -> ADMINISTRADOR_ACADEMICO
                "SUPERVISOR" -> SUPERVISOR
                "ANALISTA" -> ANALISTA
                "OPERADOR" -> OPERADOR
                "EN_ESPERA", "ESPERA", "PENDIENTE" -> EN_ESPERA
                else -> EN_ESPERA
            }
        }
    }
}

data class Usuario(
    val codigo: String,
    val nombre: String,
    val clave: String,
    val rol: Rol
)

/** Sesión activa en memoria */
object Sesion {
    var usuario: Usuario? = null
}

/** Catálogos de referencia para formularios */
object DatosDemo {
    val centros = listOf("Centro Bahía Norte", "Centro Isla Sur", "Centro Estero Azul")
    val trenes = (1..4).map { "Tren $it" }
    val lineas = (1..10).map { "Línea $it" }
}