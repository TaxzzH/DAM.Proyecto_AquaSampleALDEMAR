## COMO TRABAJAR POR RAMAS

Para trabajar en grupo sin pisarse el código, 
se recomienda optar por una estructura Por Funcionalidad (Package-by-Feature):

    com.duoc.proyectoaldemar/
    ├── data/                    # Repositorios y llamadas a API (Compartido)
    │
    ├── ui/
    │   ├── theme/               # <--- SOLAMENTE para Color.kt, Theme.kt, Type.kt (Global)
    │   ├── components/          # Botones o textos reutilizables entre pantallas
    │   │
    │   ├── login/               # <--- Integrante 1 trabaja aquí adentro
    │   │   ├── LoginScreen.kt
    │   │   ├── LoginViewModel.kt
    │   │   └── LoginState.kt
    │   │
    │   ├── home/                # <--- Integrante 2 trabaja aquí adentro
    │   │   ├── HomeScreen.kt
    │   │   └── HomeViewModel.kt
    │   │
    │   └── profile/             # <--- Integrante 3 trabaja aquí adentro
    │       ├── ProfileScreen.kt
    │       └── ProfileViewModel.kt
    │
    └── MainActivity.kt

###  Ventajas de este enfoque:

* ui/theme queda impecable: Solo contiene la identidad visual de la app 
(como vimos en las preguntas anteriores). No se mezclan pantallas ahí.

* Trabajo en paralelo sin conflictos: Si estás programando el Login dentro de
ui/login/, nunca tocarás los archivos del compañero que hace el Home dentro de ui/home/. 
Esto evita el 90% de los conflictos al hacer merge en Git.