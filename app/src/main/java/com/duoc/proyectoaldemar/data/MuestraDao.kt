package com.duoc.proyectoaldemar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.duoc.proyectoaldemar.model.Muestra
import kotlinx.coroutines.flow.Flow

@Dao
interface MuestraDao {
    @Query("SELECT * FROM muestras ORDER BY fechaHora DESC")
    fun observarTodas(): Flow<List<Muestra>>

    @Query("SELECT * FROM muestras WHERE id = :id")
    fun observar(id: Long): Flow<Muestra?>

    @Query("SELECT * FROM muestras WHERE id = :id")
    suspend fun obtener(id: Long): Muestra?

    @Query("SELECT * FROM muestras WHERE estado = 'VALIDADO' AND sincronizada = 0")
    suspend fun porSincronizar(): List<Muestra>

    /** Inserta si id = 0, actualiza si ya existe. */
    @Upsert
    suspend fun guardar(muestra: Muestra)

    @Delete
    suspend fun eliminar(muestra: Muestra)
}