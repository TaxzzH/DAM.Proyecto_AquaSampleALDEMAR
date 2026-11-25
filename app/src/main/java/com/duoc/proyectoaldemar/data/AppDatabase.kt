package com.duoc.proyectoaldemar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.duoc.proyectoaldemar.model.MarcaConteo
import com.duoc.proyectoaldemar.model.Muestra

@Database(entities = [Muestra::class, MarcaConteo::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun muestraDao(): MuestraDao
    abstract fun marcaConteoDao(): MarcaConteoDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE muestras ADD COLUMN uuid TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE muestras ADD COLUMN estadoSincronizacion TEXT NOT NULL DEFAULT 'PENDIENTE'")
                db.execSQL("UPDATE muestras SET uuid = lower(hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-4' || substr(hex(randomblob(2)), 2) || '-a' || substr(hex(randomblob(2)), 2) || '-' || hex(randomblob(6))) WHERE uuid = '' OR uuid IS NULL")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_muestras_uuid ON muestras(uuid)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `marcas_conteo` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `muestraUuid` TEXT NOT NULL,
                        `xNorm` REAL NOT NULL,
                        `yNorm` REAL NOT NULL,
                        `orden` INTEGER NOT NULL,
                        FOREIGN KEY(`muestraUuid`) REFERENCES `muestras`(`uuid`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_marcas_conteo_muestraUuid` ON `marcas_conteo` (`muestraUuid`)")
            }
        }
    }
}
