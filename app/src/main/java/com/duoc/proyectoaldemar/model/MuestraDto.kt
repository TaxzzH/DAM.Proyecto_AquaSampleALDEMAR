package com.duoc.proyectoaldemar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class MuestraDto(
    @SerialName("id") val id: String,
    @SerialName("fecha_hora") val fechaHora: Long,
    @SerialName("centro") val centro: String,
    @SerialName("tren") val tren: String,
    @SerialName("linea") val linea: String,
    @SerialName("tramo_cm") val tramoCm: Int,
    @SerialName("operador") val operador: String,
    @SerialName("conteo") val conteo: Int,
    @SerialName("calibre_mm") val calibreMm: Double? = null,
    @SerialName("foto_url") val fotoUrl: String? = null,
    @SerialName("observaciones") val observaciones: String = "",
    @SerialName("estado") val estado: String,
    @SerialName("comentario_supervisor") val comentarioSupervisor: String = ""
) {
    fun toJsonObject(): JsonObject = buildJsonObject {
        put("id", id)
        put("fecha_hora", fechaHora)
        put("centro", centro)
        put("tren", tren)
        put("linea", linea)
        put("tramo_cm", tramoCm)
        put("operador", operador)
        put("conteo", conteo)
        calibreMm?.let { put("calibre_mm", it) }
        fotoUrl?.let { put("foto_url", it) }
        put("observaciones", observaciones)
        put("estado", estado)
        put("comentario_supervisor", comentarioSupervisor)
    }
}

fun Muestra.aDto(fotoUrlRemota: String? = null): MuestraDto = MuestraDto(
    id = uuid,
    fechaHora = fechaHora,
    centro = centro,
    tren = tren,
    linea = linea,
    tramoCm = tramoCm,
    operador = operador,
    conteo = conteo,
    calibreMm = calibreMm,
    fotoUrl = fotoUrlRemota ?: fotoPath,
    observaciones = observaciones,
    estado = estado.name,
    comentarioSupervisor = comentarioSupervisor
)
