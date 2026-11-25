package com.duoc.proyectoaldemar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.proyectoaldemar.AquaSampleApp
import com.duoc.proyectoaldemar.model.Estado
import com.duoc.proyectoaldemar.model.Muestra
import com.duoc.proyectoaldemar.model.Sesion
import com.duoc.proyectoaldemar.util.Reglas
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class RangoFecha(val etiqueta: String) {
    TODO("Todo"),
    ULTIMOS_7_DIAS("Últimos 7 días"),
    ULTIMOS_30_DIAS("Últimos 30 días")
}

data class EstadoDistribucionItem(
    val estado: Estado,
    val cantidad: Int,
    val porcentaje: Float
)

data class PuntoEvolucionTemporal(
    val fechaTexto: String,
    val timestamp: Long,
    val cantidad: Int
)

data class ConteoPorCentroItem(
    val centro: String,
    val totalIndividuos: Int
)

/** Indicadores de resumen, filtros dinámicos, gráficos y sincronización con la API remota. */
class ResumenViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as AquaSampleApp).repo

    var todasLasMuestrasVisibles by mutableStateOf<List<Muestra>>(emptyList())
        private set

    var centroSeleccionado by mutableStateOf("Todos")
        private set

    var estadoSeleccionado by mutableStateOf<Estado?>(null)
        private set

    var rangoFechaSeleccionado by mutableStateOf(RangoFecha.TODO)
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf("")
        private set

    val lista: List<Muestra>
        get() {
            val ahora = System.currentTimeMillis()
            val umbralMs = when (rangoFechaSeleccionado) {
                RangoFecha.ULTIMOS_7_DIAS -> ahora - (7L * 24 * 3600 * 1000)
                RangoFecha.ULTIMOS_30_DIAS -> ahora - (30L * 24 * 3600 * 1000)
                RangoFecha.TODO -> 0L
            }

            return todasLasMuestrasVisibles.filter { m ->
                val cumpleCentro = centroSeleccionado == "Todos" || m.centro.equals(centroSeleccionado, ignoreCase = true)
                val cumpleEstado = estadoSeleccionado == null || m.estado == estadoSeleccionado
                val cumpleFecha = m.fechaHora >= umbralMs
                cumpleCentro && cumpleEstado && cumpleFecha
            }
        }

    val centrosDisponibles: List<String>
        get() {
            val deMuestras = todasLasMuestrasVisibles.map { it.centro }.filter { it.isNotBlank() }.distinct().sorted()
            return listOf("Todos") + deMuestras
        }

    val totalMuestras get() = lista.size
    val totalIndividuos get() = lista.sumOf { it.conteo }
    val densidadPromedio get() = lista.map { Reglas.densidad(it.conteo, it.tramoCm) }.average().takeIf { !it.isNaN() } ?: 0.0

    val hayFiltrosActivos: Boolean
        get() = centroSeleccionado != "Todos" || estadoSeleccionado != null || rangoFechaSeleccionado != RangoFecha.TODO

    // Visualización Gráfica A: Distribución por Estado
    val distribucionPorEstado: List<EstadoDistribucionItem>
        get() {
            val total = lista.size
            return Estado.entries.map { e ->
                val cant = lista.count { it.estado == e }
                val pct = if (total > 0) cant.toFloat() / total else 0f
                EstadoDistribucionItem(e, cant, pct)
            }
        }

    // Visualización Gráfica B: Evolución Temporal de Muestras
    val evolucionTemporal: List<PuntoEvolucionTemporal>
        get() {
            if (lista.isEmpty()) return emptyList()
            val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
            return lista
                .groupBy { m ->
                    val cal = Calendar.getInstance().apply { timeInMillis = m.fechaHora }
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    cal.timeInMillis
                }
                .toSortedMap()
                .map { (startOfDayMs, muestrasDelDia) ->
                    PuntoEvolucionTemporal(
                        fechaTexto = sdf.format(Date(startOfDayMs)),
                        timestamp = startOfDayMs,
                        cantidad = muestrasDelDia.size
                    )
                }
        }

    // Visualización Gráfica C: Individuos Registrados por Centro
    val individuosPorCentro: List<ConteoPorCentroItem>
        get() {
            if (lista.isEmpty()) return emptyList()
            return lista
                .groupBy { if (it.centro.isBlank()) "Sin Centro" else it.centro }
                .map { (centro, muestras) ->
                    ConteoPorCentroItem(
                        centro = centro,
                        totalIndividuos = muestras.sumOf { it.conteo }
                    )
                }
                .sortedByDescending { it.totalIndividuos }
        }

    init {
        viewModelScope.launch {
            repo.todas().collect { todas ->
                val u = Sesion.usuario
                todasLasMuestrasVisibles = if (u != null) {
                    todas.filter { Reglas.esVisible(u, it) }
                } else {
                    emptyList()
                }
            }
        }
    }

    fun cambiarCentro(centro: String) {
        centroSeleccionado = centro
    }

    fun cambiarEstado(estado: Estado?) {
        estadoSeleccionado = estado
    }

    fun cambiarRangoFecha(rango: RangoFecha) {
        rangoFechaSeleccionado = rango
    }

    fun limpiarFiltros() {
        centroSeleccionado = "Todos"
        estadoSeleccionado = null
        rangoFechaSeleccionado = RangoFecha.TODO
    }

    /** Maneja la sincronización de muestras validadas hacia la API. */
    fun sincronizar() {
        if (Sesion.usuario == null) {
            mensaje = "Sesión no válida. Inicia sesión nuevamente."
            return
        }
        viewModelScope.launch {
            cargando = true
            mensaje = try {
                "Sincronizadas: ${repo.sincronizar()}"
            } catch (_: Exception) {
                "No se pudo sincronizar. Revisa tu conexión."
            }
            cargando = false
        }
    }
}
