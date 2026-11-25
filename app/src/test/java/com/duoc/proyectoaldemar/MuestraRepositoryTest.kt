package com.duoc.proyectoaldemar

import com.duoc.proyectoaldemar.data.remote.InsforgeMuestraApi
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.EstadoSincronizacion
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.aDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MuestraRepositoryTest {

    @Test
    fun muestra_generacionUuid_esUnicoYValido() {
        val muestra1 = Muestra(
            fechaHora = System.currentTimeMillis(),
            centro = "Centro Bahía Norte",
            tren = "Tren 1",
            linea = "Línea 1",
            tramoCm = 20,
            operador = "OP-01",
            conteo = 150
        )
        val muestra2 = Muestra(
            fechaHora = System.currentTimeMillis(),
            centro = "Centro Isla Sur",
            tren = "Tren 2",
            linea = "Línea 3",
            tramoCm = 50,
            operador = "OP-02",
            conteo = 300
        )

        assertNotNull(muestra1.uuid)
        assertNotNull(muestra2.uuid)
        assertEquals(36, muestra1.uuid.length)
        assertNotEquals(muestra1.uuid, muestra2.uuid)
    }

    @Test
    fun muestra_mapeoADto_conservaValoresEIdentificadorUuid() {
        val muestra = Muestra(
            id = 5,
            fechaHora = 1700000000000L,
            centro = "Centro Estero Azul",
            tren = "Tren 3",
            linea = "Línea 5",
            tramoCm = 100,
            operador = "OP-01",
            conteo = 500,
            estado = Estado.VALIDADO,
            estadoSincronizacion = EstadoSincronizacion.PENDIENTE
        )

        val dto = muestra.aDto("https://kgkzid4k.us-east.insforge.app/storage/v1/object/public/muestras/foto_123.jpg")

        assertEquals(muestra.uuid, dto.id)
        assertEquals("Centro Estero Azul", dto.centro)
        assertEquals("VALIDADO", dto.estado)
        assertEquals("https://kgkzid4k.us-east.insforge.app/storage/v1/object/public/muestras/foto_123.jpg", dto.fotoUrl)
        assertEquals(500, dto.conteo)
    }

    @Test
    fun estadosIndependientes_estadoRevisionYEstadoSincronizacion() {
        val muestraBorrador = Muestra(
            fechaHora = System.currentTimeMillis(),
            centro = "Centro Bahía Norte",
            tren = "Tren 1",
            linea = "Línea 1",
            tramoCm = 20,
            operador = "OP-01",
            conteo = 0,
            estado = Estado.BORRADOR,
            estadoSincronizacion = EstadoSincronizacion.PENDIENTE
        )

        val muestraEnviada = muestraBorrador.copy(
            estado = Estado.PENDIENTE,
            estadoSincronizacion = EstadoSincronizacion.SINCRONIZADA,
            sincronizada = true
        )

        assertEquals(Estado.BORRADOR, muestraBorrador.estado)
        assertEquals(EstadoSincronizacion.PENDIENTE, muestraBorrador.estadoSincronizacion)
        assertFalse(muestraBorrador.sincronizada)

        assertEquals(Estado.PENDIENTE, muestraEnviada.estado)
        assertEquals(EstadoSincronizacion.SINCRONIZADA, muestraEnviada.estadoSincronizacion)
        assertTrue(muestraEnviada.sincronizada)
    }

    @Test
    fun insforgeApi_pruebasDetalladasDeteccionDuplicados() {
        val api = InsforgeMuestraApi()

        // 1. Un error con código 23505 se reconoce como duplicación -> true
        val ex23505 = Exception("Database error code 23505: duplicate key value violates unique constraint")
        assertTrue(api.esErrorConflictoClaveDuplicada(ex23505))

        // 2. Una excepción UnknownHostException no se reconoce como duplicación -> false
        val exUnknown = UnknownHostException("Unable to resolve host")
        assertFalse(api.esErrorConflictoClaveDuplicada(exUnknown))

        // 3. Una ConnectException no se reconoce como duplicación -> false
        val exConnect = ConnectException("Connection refused")
        assertFalse(api.esErrorConflictoClaveDuplicada(exConnect))

        // 4. Una SocketTimeoutException no se reconoce como duplicación -> false
        val exTimeout = SocketTimeoutException("Read timed out")
        assertFalse(api.esErrorConflictoClaveDuplicada(exTimeout))

        // 5. Un error 401 o 403 no se reconoce como duplicación -> false
        val ex401 = Exception("HTTP 401 Unauthorized")
        val ex403 = Exception("HTTP 403 Forbidden")
        assertFalse(api.esErrorConflictoClaveDuplicada(ex401))
        assertFalse(api.esErrorConflictoClaveDuplicada(ex403))

        // 6. Un mensaje que contenga accidentalmente las letras "pk" no se reconoce como duplicación -> false
        val exPk = Exception("Package configuration error or public key expired")
        assertFalse(api.esErrorConflictoClaveDuplicada(exPk))

        // 7. Los errores generales del servidor no provocan una actualización automática (ej. 500) -> false
        val ex500 = Exception("HTTP 500 Internal Server Error")
        assertFalse(api.esErrorConflictoClaveDuplicada(ex500))
    }
}
