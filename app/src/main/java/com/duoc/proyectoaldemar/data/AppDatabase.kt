package com.duoc.proyectoaldemar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duoc.proyectoaldemar.model.Muestra

@Database(entities = [Muestra::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun muestraDao(): MuestraDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE muestras ADD COLUMN uuid TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE muestras ADD COLUMN estadoSincronizacion TEXT NOT NULL DEFAULT 'PENDIENTE'")
                db.execSQL("UPDATE muestras SET uuid = lower(hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-4' || substr(hex(randomblob(2)), 2) || '-a' || substr(hex(randomblob(2)), 2) || '-' || hex(randomblob(6))) WHERE uuid = '' OR uuid IS NULL")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_muestras_uuid ON muestras(uuid)")
            }
        }
    }
}
