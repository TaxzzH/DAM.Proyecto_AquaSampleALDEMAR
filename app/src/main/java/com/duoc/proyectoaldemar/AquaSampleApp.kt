package com.duoc.proyectoaldemar

import android.app.Application
import androidx.room.Room
import com.duoc.proyectoaldemar.data.AppDatabase
import com.duoc.proyectoaldemar.data.MuestraRepository
import com.duoc.proyectoaldemar.data.crearSyncApi
import com.duoc.proyectoaldemar.data.worker.SyncWorker

/** Crea una sola vez el repositorio; los ViewModel lo toman de aquí. */
class AquaSampleApp : Application() {
    val repo by lazy {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "aquasample.db")
            .addMigrations(AppDatabase.MIGRATION_1_2, AppDatabase.MIGRATION_2_3)
            .build()
        MuestraRepository(
            dao = db.muestraDao(),
            api = crearSyncApi(),
            marcaDao = db.marcaConteoDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Programar sincronización de muestras pendientes al iniciar la aplicación
        SyncWorker.programarSincronizacion(this)
    }
}
