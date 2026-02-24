# 🧭 Navi

Aplicación Android desarrollada en **Kotlin** orientada a la **navegación colaborativa ligera**.
Proyecto académico centrado en el uso de **servicios en la nube**, **sensores del dispositivo móvil** y **arquitectura Android moderna**.

Este repositorio documenta el desarrollo incremental de la aplicación mediante hitos (*milestones*).

---

## 🗺️ Milestone 1 — Google Maps base y app corriendo.
- 📱 Creación del proyecto Android (Kotlin, Empty Views Activity, minSdk 26)
- 🧩 Integración del **Google Maps SDK for Android**
- 🔐 Configuración segura de la **API Key** y restricción en **Google Cloud Platform**
- 🗺️ Visualización funcional del mapa en pantalla, con centrado inicial en Sevilla

## 📍 Milestone 2 — Ubicación del usuario y modo seguimiento

- 🔐 Solicitud de permisos de ubicación en tiempo de ejecución (FINE / COARSE)
- 📌 Activación de la capa **My Location** (punto azul)
- 🎯 Botón flotante para centrar en la ubicación actual
- 🧭 Implementación de **Follow Mode** (seguimiento en tiempo real)
- ✋ Desactivación automática del seguimiento al detectar gesto manual en el mapa
- 📲 Pruebas realizadas en dispositivo físico (Samsung S23)
- 🔑 Gestión segura de la API Key mediante `manifestPlaceholders` y `local.properties` (no expuesta en el repositorio)

---

## 🔑 Configuración local de Google Maps API Key

La clave de Google Maps no se incluye en el repositorio.

Para ejecutar el proyecto:

1. Copia el archivo `local.properties.example`
2. Renómbralo a `local.properties`
3. Sustituye el valor de `MAPS_API_KEY` por tu propia clave
    * MAPS_API_KEY=TU_CLAVE_AQUI

Asegúrate de que la clave tenga habilitada la API:
- Maps SDK for Android
- Restricción por Android App (package + SHA-1)

---

## ⚙️ Stack tecnológico

### 📱 Desarrollo Android
- 🧠 **Kotlin** — lenguaje principal de programación
- 🏗️ **Android Studio** — entorno de desarrollo
- 🧩 **AndroidX** — gestión moderna de Activities y Fragments

### 🔧 Build & despliegue
- 📦 **Gradle (Kotlin DSL)** — gestión de dependencias y build
- 🔄 **Gradle Sync** — automatización del proceso de compilación
- 🗂️ **Git + GitHub** — control de versiones y seguimiento de hitos

### ☁️ Servicios en la nube
- ☁️ **Google Cloud Platform**
    - 🗺️ **Maps SDK for Android** — proveedor cartográfico
    - 🔐 Gestión de credenciales y API Keys

---

## 🚀 Estado actual
La aplicación muestra correctamente un mapa interactivo de Google Maps, confirmando la correcta
integración entre la aplicación Android y los servicios en la nube.

Este hito establece una base sólida para implementar funcionalidades colaborativas
y personalización mediante sensores del dispositivo en iteraciones posteriores.
