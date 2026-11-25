package com.duoc.proyectoaldemar.data.remote

import com.duoc.proyectoaldemar.data.InsforgeProvider
import com.duoc.proyectoaldemar.model.MuestraDto
import dev.insforge.database.database
import dev.insforge.storage.storage
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class InsforgeMuestraApi {

    /**
     * Sube el archivo de imagen al bucket de InsForge Storage 'muestras' (si existe y la foto no es nula).
     * Retorna la URL pública remota si se sube con éxito, o null si falla.
     */
    suspend fun subirFotografia(fotoPathLocal: String?, uuidMuestra: String): String? {
        if (fotoPathLocal.isNullOrBlank()) return null
        val archivo = File(fotoPathLocal)
        if (!archivo.exists()) return null

        val bucketNombre = "muestras"
        val nombreRemoto = "foto_$uuidMuestra.jpg"

        return try {
            val bytes = archivo.readBytes()
            val res = InsforgeProvider.client.storage.from(bucketNombre)
                .upload(nombreRemoto, bytes)
            res.toString()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Envía una muestra a la tabla remota 'muestras' de InsForge.
     * Solo ejecuta actualización cuando la excepción indique explícitamente un conflicto por clave única (23505),
     * evitando reintentar la actualización ante errores de red, autenticación, permisos o fallos generales del servidor.
     */
    suspend fun enviarMuestra(dto: MuestraDto): Boolean {
        val jsonObj = dto.toJsonObject()
        return try {
            InsforgeProvider.client.database.from("muestras")
                .insert(buildJsonArray { add(jsonObj) })
                .execute<JsonElement>()
            true
        } catch (e: Exception) {
            if (esErrorConflictoClaveDuplicada(e)) {
                try {
                    InsforgeProvider.client.database.from("muestras")
                        .eq("id", dto.id)
                        .update(jsonObj)
                        .execute<JsonElement>()
                    true
                } catch (_: Exception) {
                    false
                }
            } else {
                false
            }
        }
    }

    internal fun esErrorConflictoClaveDuplicada(e: Exception): Boolean {
        if (e is UnknownHostException ||
            e is ConnectException ||
            e is SocketTimeoutException) {
            return false
        }

        var current: Throwable? = e
        while (current != null) {
            if (current is UnknownHostException ||
                current is ConnectException ||
                current is SocketTimeoutException) {
                return false
            }

            val msg = current.message?.lowercase() ?: ""

            if (msg.contains("401") || msg.contains("403") || msg.contains("unauthorized") ||
                msg.contains("forbidden") || msg.contains("500") || msg.contains("502") ||
                msg.contains("503") || msg.contains("504")) {
                return false
            }

            if (msg.contains("23505") ||
                msg.contains("duplicate key") ||
                msg.contains("unique constraint") ||
                msg.contains("already exists")) {
                return true
            }

            current = current.cause
        }

        return false
    }
}
