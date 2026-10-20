package com.duoc.proyectoaldemar.util

import com.duoc.proyectoaldemar.model.Formulario

object Validador {

    /**
     * Devuelve un mapa campo -> mensaje de error. Mapa vacío = formulario válido.
     * Al enviar a revisión (enviar = true) exige conteo > 0 y foto.
     */
    fun validar(f: Formulario, enviar: Boolean): Map<String, String> = buildMap {
        if (f.centro.isBlank()) put("centro", "Selecciona el centro")
        if (f.tren.isBlank()) put("tren", "Selecciona el tren")
        if (f.linea.isBlank()) put("linea", "Selecciona la línea")

        val tramoNum = f.tramo.toIntOrNull()
        if (tramoNum == null || tramoNum !in 1..200) put("tramo", "El tramo debe estar entre 1 y 200 cm")

        val conteo = f.conteo.toIntOrNull()
        if (conteo == null || conteo !in 0..5000) put("conteo", "El conteo debe estar entre 0 y 5000")
        else if (enviar && conteo == 0) put("conteo", "El conteo debe ser mayor a 0 para enviar")

        if (f.calibre.isNotBlank()) {
            val cal = f.calibre.replace(',', '.').toDoubleOrNull()
            if (cal == null || cal <= 0 || cal > 150) put("calibre", "El calibre debe estar entre 0 y 150 mm")
        }

        if (f.observaciones.length > 300) put("observaciones", "Máximo 300 caracteres")
        if (enviar && f.fotoPath == null) put("foto", "Debes adjuntar una fotografía")
    }

    fun comentarioValido(texto: String) = texto.trim().length >= 5
}