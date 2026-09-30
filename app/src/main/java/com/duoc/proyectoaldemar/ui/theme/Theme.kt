package com.duoc.proyectoaldemar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    secondary = SecColor,
    background = FondoGeneralColor,
    surface = FondoGeneralColor,
    onBackground = TextColor,     // "onBackground" es el color del texto sobre el fondo
    onPrimary = Color.White       // Texto que vaya DENTRO de un botón primario
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkMainColor,
    secondary = DarkSecColor,
    background = DarkFondoGeneralColor,
    surface = DarkDetallesColor,
    onBackground = DarkTextColor, // Texto sobre fondo oscuro
    onPrimary = Color(0xFF003554)  // Texto sobre el botón principal en modo oscuro
)

@Composable
fun EP2_ProyectoALDEMARTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // <--- 1. Pregunta al teléfono
    content: @Composable () -> Unit
) {
    // 2. Elige la paleta según el resultado
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    // 3. Provee la paleta elegida a TODA la app
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}