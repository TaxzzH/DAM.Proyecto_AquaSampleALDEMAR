package com.duoc.proyectoaldemar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duoc.proyectoaldemar.model.Muestra

@Database(entities = [Muestra::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun muestraDao(): MuestraDao
}