package com.duoc.proyectoaldemar.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/** Archivos de fotografía en el almacenamiento interno de la app. */
object Fotos {

    fun nuevoArchivo(context: Context): File =
        File(File(context.filesDir, "fotos").apply { mkdirs() }, "muestra_${System.currentTimeMillis()}.jpg")

    /** Uri que la app de cámara usa para escribir la foto en nuestro archivo. */
    fun uriPara(context: Context, archivo: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)

    /** Copia la imagen elegida en la galería a nuestra carpeta para no perder el acceso. */
    fun copiarDesdeGaleria(context: Context, uri: Uri): File {
        val destino = nuevoArchivo(context)
        context.contentResolver.openInputStream(uri)!!.use { entrada ->
            destino.outputStream().use { entrada.copyTo(it) }
        }
        return destino
    }
}