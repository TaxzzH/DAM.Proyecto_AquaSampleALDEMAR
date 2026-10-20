package com.duoc.proyectoaldemar.model

/** Lo que el usuario escribe en la pantalla. Todo es texto para poder validarlo antes de convertirlo. */
data class Formulario(
    val id: Long = 0,
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
    val comentarioSupervisor: String = ""
) {
    fun aMuestra(estado: Estado) = Muestra(
        id = id,
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
        comentarioSupervisor = comentarioSupervisor
    )

    companion object {
        fun desde(m: Muestra) = Formulario(
            id = m.id,
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
            comentarioSupervisor = m.comentarioSupervisor
        )
    }
}