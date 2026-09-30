## ¿Cómo usas esto en tus pantallas?

Cuando programas la interfaz, nunca llamas directamente a MainColor o 
DarkMainColor. En su lugar, usas "MaterialTheme.colorScheme.*" .

Kotlin

    // Tu pantalla
    Surface(

    // Toma automáticamente FondoGeneralColor (Claro) o DarkFondoGeneralColor (Oscuro)
    color = MaterialTheme.colorScheme.background

    ) {
    Column {
    Text(
    text = "Bienvenido a la App",

    // Toma automáticamente TextColor (Oscuro) o DarkTextColor (Claro)
    color = MaterialTheme.colorScheme.onBackground
    )
    
            Button(
                onClick = { /* action */ },

                // El botón usará MainColor en modo claro y DarkMainColor en modo oscuro
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Iniciar Sesión")
            }
        }
    }


* Al hacerlo así, en cuanto el usuario cambia el interruptor de Modo Oscuro en la 
barra de notificaciones de Android, Compose reacciona automáticamente y redibuja 
toda la pantalla con los nuevos colores sin necesidad de reiniciar la app.