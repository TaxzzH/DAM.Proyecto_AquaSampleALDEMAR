<div align="center">

# 🌊 AquaTraz
### Sistema Móvil de Registro y Trazabilidad de Muestras de Choritos

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%2520Compose-M3-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![Room](https://img.shields.io/badge/Room-2.8.5-0077B6?style=flat-square&logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![InsForge](https://img.shields.io/badge/Backend-InsForge-FF6F61?style=flat-square)](https://insforge.dev)

</div>

---

## 📋 Descripción del Proyecto

**AquaTraz** es una aplicación móvil desarrollada en Android para optimizar los procesos de recolección, registro y trazabilidad de muestras de choritos de la empresa **ALDEMAR SpA**. El sistema reemplaza los flujos de registro manuales tradicionales por una solución digital moderna, rápida y estructurada.

Permite a los operadores en terreno capturar información de cultivo (centro, tren, línea, tramo, calibre y conteo), adjuntar evidencias fotográficas mediante **CameraX** o galería, realizar **conteo asistido manual** con marcas normalizadas sobre la fotografía, y trabajar en modalidad **offline-first** con sincronización automática en segundo plano mediante **WorkManager** e **InsForge**. Supervisores y analistas disponen de un **Dashboard Avanzado** con indicadores KPI, filtros dinámicos y gráficos analíticos interactivos.

---

## ✨ Funcionalidades Principales

* **Autenticación y Roles:** Gestión de acceso basada en roles definidos (`Operador`, `Supervisor`, `Analista`, `Administrador Académico`).
* **Formulario de Muestras:** Registro detallado por Centro de cultivo, Tren, Línea, Tramo (cm), Calibre (mm) y Observaciones.
* **Captura con CameraX:** Interfaz de cámara integrada con vista previa en tiempo real, control de ciclo de vida y optimización de memoria.
* **Selección desde Galería:** Soporte para adjuntar imágenes existentes con persistencia en el almacenamiento interno privado.
* **Conteo Asistido sobre Fotografías:** Herramienta interactiva con zoom multitáctil ($1.0\times$ a $5.0\times$), paneo, marcas numeradas consecutivas con coordenadas normalizadas ($0.0$ a $1.0$), deshacer, borrar marca individual y ajuste manual del conteo final corregido.
* **Persistencia Local Offline-First:** Base de datos **Room** como única fuente de verdad local, garantizando disponibilidad sin conexión a internet.
* **Sincronización en Segundo Plano:** Sincronización automática con reintentos exponenciales mediante **WorkManager** y backend **InsForge**.
* **Dashboard Avanzado con Indicadores KPI:** Tarjetas de resumen para Total de Muestras, Total de Individuos y Densidad Promedio (`ind/m`).
* **Filtros Dinámicos Combinados:** Filtrado reactivo en tiempo real por Centro de cultivo, Estado de revisión (`Borrador`, `Pendiente`, `Observado`, `Corregido`, `Validado`) y Rango de fechas (*Todo*, *Últimos 7 días*, *Últimos 30 días*).
* **Gráficos Estadísticos Nativos:**
  * Distribución de muestras por estado con barras de progreso y porcentajes.
  * Evolución temporal de muestras mediante gráficos de barras interactivos en Canvas.
  * Comparativa de individuos registrados por centro de cultivo.

---

## 🛠️ Tecnologías y Librerías

| Tecnología | Propósito en el Proyecto | Versión |
| :--- | :--- | :--- |
| **Kotlin** | Lenguaje principal de desarrollo | `2.2.10` |
| **Jetpack Compose** | Toolkit declarativo de UI (Material Design 3) | BOM `2026.02.01` |
| **Android Gradle Plugin (AGP)** | Sistema de compilación y empaquetado | `9.4.1` |
| **Room** | ORM de base de datos local y migraciones (v1 $\rightarrow$ v3) | `2.8.5` |
| **CameraX** | Captura de fotografías integrada | `1.7.0` |
| **WorkManager** | Sincronización asíncrona en segundo plano | `2.10.0` |
| **Retrofit & Gson** | Consumo de servicios API REST | `2.11.0` |
| **Coil** | Carga y caché asíncrona de imágenes | `2.7.0` |
| **InsForge Kotlin SDK** | Integración con backend y almacenamiento | `0.1.7` |

---

## 🏛️ Arquitectura del Software

El proyecto sigue estrictamente el patrón arquitectónico **MVVM (Model-View-ViewModel)** y buenas prácticas recomendadas por Android:

```text
com.duoc.proyectoaldemar/
├── data/           # DAOs de Room, repositorios, API y sincronización WorkManager
├── model/          # Entidades Room (@Entity), DTOs y enumeraciones de dominio
├── ui/             # Pantallas en Jetpack Compose (Inicio, Nueva Muestra, Conteo, Detalle, Resumen, Admin)
├── util/           # Utilidades (Gestión de fotos, validadores, reglas de negocio)
└── viewmodel/      # ViewModels asociados a cada flujo de la aplicación (MVVM)
```

---

## 🔄 Persistencia y Sincronización Offline-First

1. **Almacenamiento Local:** Las muestras y marcas de conteo se almacenan de forma local en **Room**. Las fotografías capturadas se guardan en el directorio privado interno (`context.filesDir/fotos/`).
2. **Sincronización:** Las muestras completadas (no borradores) se sincronizan con **InsForge** mediante `SyncWorker`, aplicando la estrategia `APPEND_OR_REPLACE` y reintentos exponenciales. Las marcas de conteo se preservan de forma robusta a nivel local.

---

## 🎨 Identidad Visual (Paleta de Colores)

El diseño de AquaTraz está alineado con la identidad acuícola e institucional de ALDEMAR SpA:
* **Principal (`#0077B6`):** Botones principales, encabezados y elementos destacados.
* **Secundario (`#00B4D8`):** Iconos, acentos y barras de progreso.
* **Acento (`#48CAE4`):** Detalles visuales y elementos de apoyo.
* **Fondo (`#F5F9FC`):** Superficie clara de las pantallas.
* **Texto (`#1B1B1B`):** Títulos y textos principales.

---

## 🚀 Requisitos Previos

* **Android Studio** (versión recomendada: Jellyfish o superior).
* **JDK 17**.
* Dispositivo físico Android o emulador con **API 26 (Android 8.0)** como mínimo (`minSdk = 26`, `targetSdk = 35`, `compileSdk = 37`).

---

## 📥 Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
   ```
2. Abrir **Android Studio** y seleccionar **Open an Existing Project** apuntando a la carpeta raíz del proyecto.
3. Esperar a que **Gradle** sincronice las dependencias automáticamente.
4. Conectar un emulador o dispositivo Android compatible.
5. Ejecutar la aplicación presionando el botón **Run (`Shift + F10`)** en Android Studio.

---

## 🧪 Pruebas Automatizadas

El proyecto incluye pruebas unitarias automatizadas que cubren la lógica de repositorio, generación de UUIDs, validación de DTOs, manejo de excepciones de duplicados, validación de coordenadas normalizadas, operaciones de conteo asistido, migraciones de base de datos y cálculos del Dashboard.

Para ejecutar las pruebas unitarias desde la terminal o el IDE:
```bash
# Compilar la aplicación en modo Debug
./gradlew :app:assembleDebug

# Ejecutar pruebas unitarias locales
./gradlew :app:testDebugUnitTest
```
*(Estado actual: Compilación exitosa y **25/25 pruebas unitarias aprobadas**).*

---

## 👥 Autoría y Contexto Académico

* **Institución:** Duoc UC
* **Proyecto:** AquaTraz (EP2 - Desarrollo Móvil Android)
* **Organización Asociada:** ALDEMAR SpA
* **Equipo de Desarrollo:** **Six Sevenardos (N° 16)** | **Sección:** 004V
  * **Tomas Herrera** — Desarrollador Backend
  * **Ignacio Retamales** — Desarrollador Frontend

---
<div align="center">
  <sub>Desarrollado con Jetpack Compose y Kotlin para ALDEMAR SpA.</sub>
</div>
