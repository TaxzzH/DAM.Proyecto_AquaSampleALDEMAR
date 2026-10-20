package com.duoc.proyectoaldemar.util

import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.Rol
import com.duoc.proyectoaldemar.model.Usuario

object Reglas {

    fun puedeEditar(u: Usuario, m: Muestra) =
        u.rol == Rol.OPERADOR && m.operador == u.codigo &&
                m.estado in listOf(Estado.BORRADOR, Estado.OBSERVADO)

    fun puedeEliminar(u: Usuario, m: Muestra) =
        u.rol == Rol.OPERADOR && m.operador == u.codigo && m.estado == Estado.BORRADOR

    fun puedeRevisar(u: Usuario, m: Muestra) =
        u.rol == Rol.SUPERVISOR && m.estado in listOf(Estado.PENDIENTE, Estado.CORREGIDO)

    /** Una muestra observada que se reenvía pasa a CORREGIDO; cualquier otra a PENDIENTE. */
    fun estadoAlEnviar(actual: Estado) =
        if (actual == Estado.OBSERVADO) Estado.CORREGIDO else Estado.PENDIENTE

    /** Filtra qué registros ve cada rol según las especificaciones del caso. */
    fun esVisible(u: Usuario, m: Muestra) = when (u.rol) {
        Rol.OPERADOR -> m.operador == u.codigo
        Rol.SUPERVISOR -> m.estado != Estado.BORRADOR
        Rol.ANALISTA -> m.estado == Estado.VALIDADO
    }

    /** Individuos de choritos por metro de cuerda */
    fun densidad(conteo: Int, tramoCm: Int) = if (tramoCm <= 0) 0.0 else conteo * 100.0 / tramoCm

    fun filtrar(lista: List<Muestra>, texto: String, estado: Estado?): List<Muestra> {
        val t = texto.trim().lowercase()
        return lista.filter { m ->
            (estado == null || m.estado == estado) &&
                    (t.isEmpty() || listOf(m.centro, m.tren, m.linea, m.operador).any { it.lowercase().contains(t) })
        }
    }
}