package com.duoc.proyectoaldemar.data

import com.duoc.proyectoaldemar.model.Muestra
import java.io.File

/** Único punto de acceso a los datos: Room (local) y Retrofit (remoto). */
class MuestraRepository(private val dao: MuestraDao, private val api: SyncApi) {

    fun todas() = dao.observarTodas()
    fun observar(id: Long) = dao.observar(id)
    suspend fun obtener(id: Long) = dao.obtener(id)
    suspend fun guardar(m: Muestra) = dao.guardar(m)

    suspend fun eliminar(m: Muestra) {
        m.fotoPath?.let { File(it).delete() }
        dao.eliminar(m)
    }

    /** Envía las muestras validadas que faltan por sincronizar. Devuelve cuántas envió. */
    suspend fun sincronizar(): Int {
        val pendientes = dao.porSincronizar()
        pendientes.forEach { api.enviar(it); dao.guardar(it.copy(sincronizada = true)) }
        return pendientes.size
    }
}