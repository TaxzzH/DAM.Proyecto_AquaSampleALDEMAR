package com.duoc.proyectoaldemar

import android.app.Application
import androidx.room.Room
import com.duoc.proyectoaldemar.data.AppDatabase
import com.duoc.proyectoaldemar.data.MuestraRepository
import com.duoc.proyectoaldemar.data.crearSyncApi

/** Crea una sola vez el repositorio; los ViewModel lo toman de aquí. */
class AquaSampleApp : Application() {
    val repo by lazy {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "aquasample.db").build()
        MuestraRepository(db.muestraDao(), crearSyncApi())
    }
}