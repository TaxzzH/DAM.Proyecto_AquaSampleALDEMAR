package com.duoc.proyectoaldemar.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

enum class Estado(val etiqueta: String) {
    BORRADOR("Borrador"),
    PENDIENTE("Pendiente"),
    OBSERVADO("Observado"),
    CORREGIDO("Corregido"),
    VALIDADO("Validado")
}

enum class EstadoSincronizacion(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    SINCRONIZADA("Sincronizada"),
    ERROR("Error")
}

/** Una muestra de choritos: tramo de cuerda contado por un operador en terreno. */
@Entity(
    tableName = "muestras",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class Muestra(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uuid: String = UUID.randomUUID().toString(),
    val fechaHora: Long,
    val centro: String,
    val tren: String,
    val linea: String,
    val tramoCm: Int,
    val operador: String,
    val conteo: Int,
    val calibreMm: Double? = null,
    val fotoPath: String? = null,
    val observaciones: String = "",
    val estado: Estado = Estado.BORRADOR,
    val comentarioSupervisor: String = "",
    val sincronizada: Boolean = false,
    val estadoSincronizacion: EstadoSincronizacion = EstadoSincronizacion.PENDIENTE
)
