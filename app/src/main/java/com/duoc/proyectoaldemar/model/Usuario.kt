package com.duoc.proyectoaldemar.model

enum class Rol(val etiqueta: String) {
    OPERADOR("Operador"),
    SUPERVISOR("Supervisor"),
    ANALISTA("Analista")
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

/** Datos FICTICIOS para el MVP según la pauta académica */
object DatosDemo {
    val usuarios = listOf(
        Usuario("OP-01", "Operador Uno", "1234", Rol.OPERADOR),
        Usuario("OP-02", "Operador Dos", "1234", Rol.OPERADOR),
        Usuario("SUP-01", "Supervisor Uno", "1234", Rol.SUPERVISOR),
        Usuario("AN-01", "Analista Uno", "1234", Rol.ANALISTA)
    )
    val centros = listOf("Centro Bahía Norte", "Centro Isla Sur", "Centro Estero Azul")
    val trenes = (1..4).map { "Tren $it" }
    val lineas = (1..10).map { "Línea $it" }
}