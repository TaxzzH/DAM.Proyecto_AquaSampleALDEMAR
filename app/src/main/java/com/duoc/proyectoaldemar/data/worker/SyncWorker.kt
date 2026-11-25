package com.duoc.proyectoaldemar.data.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.duoc.proyectoaldemar.AquaSampleApp
import java.util.concurrent.TimeUnit

/**
 * Worker para ejecutar la sincronización automática de muestras pendientes hacia InsForge
 * cuando el dispositivo cuenta con conexión a internet.
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val repo = (applicationContext as AquaSampleApp).repo
            repo.sincronizar()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AquaTrazSyncWorker"

        /**
         * Encola una tarea única de sincronización con restricciones de conectividad,
         * política APPEND_OR_REPLACE para garantizar que ninguna solicitud se pierda
         * sin cancelar procesos activos, y reintento exponencial ante fallas de red.
         */
        fun programarSincronizacion(context: Context) {
            val restricciones = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val solicitud = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(restricciones)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                solicitud
            )
        }
    }
}
