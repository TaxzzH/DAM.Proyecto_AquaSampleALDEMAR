package com.duoc.proyectoaldemar.model

import java.util.UUID

/** Lo que el usuario escribe en la pantalla. Todo es texto para poder validarlo antes de convertirlo. */
data class Formulario(
    val id: Long = 0,
    val uuid: String = "",
    val fechaHora: Long = System.currentTimeMillis(),
    val operador: String = "",
    val centro: String = "",
    val tren: String = "",
    val linea: String = "",
    val tramo: String = "20",
    val conteo: String = "0",
    val calibre: String = "",
    val observaciones: String = "",
    val fotoPath: String? = null,
    val estado: Estado = Estado.BORRADOR,
    val comentarioSupervisor: String = "",
    val estadoSincronizacion: EstadoSincronizacion = EstadoSincronizacion.PENDIENTE
) {
    fun aMuestra(estado: Estado) = Muestra(
        id = id,
        uuid = uuid.ifBlank { UUID.randomUUID().toString() },
        fechaHora = fechaHora,
        centro = centro,
        tren = tren,
        linea = linea,
        tramoCm = tramo.toIntOrNull() ?: 20,
        operador = operador,
        conteo = conteo.toIntOrNull() ?: 0,
        calibreMm = calibre.replace(',', '.').toDoubleOrNull(),
        fotoPath = fotoPath,
        observaciones = observaciones.trim(),
        estado = estado,
        comentarioSupervisor = comentarioSupervisor,
        estadoSincronizacion = if (estado == Estado.BORRADOR) EstadoSincronizacion.PENDIENTE else estadoSincronizacion
    )

    companion object {
        fun desde(m: Muestra) = Formulario(
            id = m.id,
            uuid = m.uuid,
            fechaHora = m.fechaHora,
            operador = m.operador,
            centro = m.centro,
            tren = m.tren,
            linea = m.linea,
            tramo = m.tramoCm.toString(),
            conteo = m.conteo.toString(),
            calibre = m.calibreMm?.toString() ?: "",
            observaciones = m.observaciones,
            fotoPath = m.fotoPath,
            estado = m.estado,
            comentarioSupervisor = m.comentarioSupervisor,
            estadoSincronizacion = m.estadoSincronizacion
        )
    }
}
