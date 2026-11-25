package com.duoc.proyectoaldemar.data

import com.duoc.proyectoaldemar.data.remote.InsforgeMuestraApi
import com.duoc.proyectoaldemar.model.EstadoSincronizacion
import com.duoc.proyectoaldemar.model.MarcaConteo
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.aDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.io.File

/** Único punto de acceso a los datos: Room (local) e InsForge (remoto). */
class MuestraRepository(
    private val dao: MuestraDao,
    private val api: SyncApi,
    private val insforgeApi: InsforgeMuestraApi = InsforgeMuestraApi(),
    private val marcaDao: MarcaConteoDao? = null
) {

    fun todas() = dao.observarTodas()
    fun observar(id: Long) = dao.observar(id)
    suspend fun obtener(id: Long) = dao.obtener(id)
    suspend fun guardar(m: Muestra) = dao.guardar(m)

    fun observarMarcas(muestraUuid: String): Flow<List<MarcaConteo>> =
        marcaDao?.observarPorMuestra(muestraUuid) ?: emptyFlow()

    suspend fun obtenerMarcas(muestraUuid: String): List<MarcaConteo> =
        marcaDao?.obtenerPorMuestra(muestraUuid) ?: emptyList()

    suspend fun guardarMarcas(muestraUuid: String, marcas: List<MarcaConteo>) {
        marcaDao?.reemplazarMarcasPorMuestra(muestraUuid, marcas)
    }

    suspend fun eliminar(m: Muestra) {
        m.fotoPath?.let { File(it).delete() }
        dao.eliminar(m)
    }

    /**
     * Envía las muestras pendientes a InsForge.
     * Sube primero la fotografía si existe y asigna la URL remota antes de enviar los datos.
     * Actualiza el estado de sincronización localmente únicamente tras confirmación exitosa del servidor.
     */
    suspend fun sincronizar(): Int {
        val pendientes = dao.porSincronizar()
        var exitosas = 0

        for (m in pendientes) {
            dao.actualizarEstadoSincronizacion(m.uuid, EstadoSincronizacion.EN_PROCESO, false)

            var fotoUrlRemota = m.fotoPath
            if (!m.fotoPath.isNullOrBlank()) {
                val urlRemota = insforgeApi.subirFotografia(m.fotoPath, m.uuid)
                if (urlRemota != null) {
                    fotoUrlRemota = urlRemota
                }
            }

            val exito = insforgeApi.enviarMuestra(m.aDto(fotoUrlRemota = fotoUrlRemota))

            if (exito) {
                dao.actualizarEstadoSincronizacion(m.uuid, EstadoSincronizacion.SINCRONIZADA, true)
                exitosas++
            } else {
                dao.actualizarEstadoSincronizacion(m.uuid, EstadoSincronizacion.ERROR, false)
            }
        }
        return exitosas
    }
}
