# 📚 Quiz Game – Memoria del Proyecto

## 📌 Índice
1. [Descripción general de la aplicación](#descripción-general-de-la-aplicación)
2. [Arquitectura del proyecto](#arquitectura-del-proyecto)
3. [Pantallas de la aplicación](#pantallas-de-la-aplicación)
4. [Clases principales](#clases-principales)
5. [Gestión de datos con Room](#gestión-de-datos-con-room)
6. [Cambio de idioma](#cambio-de-idioma)
7. [Diagrama 1: Arquitectura general](#diagrama-1-arquitectura-general)
8. [Diagrama 2: Flujo de navegación](#diagrama-2-flujo-de-navegación)
9. [Entorno de pruebas](#entorno-de-pruebas)
10. [Dificultades y soluciones aplicadas](#dificultades-y-soluciones-aplicadas)
11. [Conclusiones](#conclusiones)
12. [Bibliografía (APA7)](#bibliografia)

---

## 📝 Descripción general de la aplicación

**Quiz Game** es una aplicación Android desarrollada para la asignatura *Laboratorio de Desarrollo Mobile*.  
Permite al usuario realizar un cuestionario de 3 a 10 preguntas, obtener una puntuación final y consultar un historial de partidas almacenado localmente mediante **Room Database**.

El juego incluye tres tipos de preguntas:
- Pregunta + opciones en texto
- Imagen + opciones en texto
- Pregunta + opciones con imágenes

Además, integra:
- Cambio dinámico de idioma (ES/EN)
- Persistencia de resultados
- Gestión de sonido y música mediante `SoundPlayer` y `MusicPlayer`
- UI completa en Material Design 3

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una arquitectura **MVVM ligera**:

# 📚 Quiz Game – Memoria del Proyecto

## 📌 Índice
1. [Descripción general de la aplicación](#descripción-general-de-la-aplicación)
2. [Arquitectura del proyecto](#arquitectura-del-proyecto)
3. [Pantallas de la aplicación](#pantallas-de-la-aplicación)
4. [Clases principales](#clases-principales)
5. [Gestión de datos con Room](#gestión-de-datos-con-room)
6. [Cambio de idioma](#cambio-de-idioma)
7. [Diagrama 1: Arquitectura general](#diagrama-1-arquitectura-general)
8. [Diagrama 2: Flujo de navegación](#diagrama-2-flujo-de-navegación)
9. [Entorno de pruebas](#entorno-de-pruebas)
10. [Dificultades y soluciones aplicadas](#dificultades-y-soluciones-aplicadas)
11. [Conclusiones](#conclusiones)
12. [Bibliografía (APA7)](#bibliografia)

---

## 📝 Descripción general de la aplicación

**Quiz Game** es una aplicación Android desarrollada para la asignatura *Laboratorio de Desarrollo Mobile*.  
Permite al usuario realizar un cuestionario de 3 a 10 preguntas, obtener una puntuación final y consultar un historial de partidas almacenado localmente mediante **Room Database**.

El juego incluye tres tipos de preguntas:
- Pregunta + opciones en texto
- Imagen + opciones en texto
- Pregunta + opciones con imágenes

Además, integra:
- Cambio dinámico de idioma (ES/EN)
- Persistencia de resultados
- Gestión de sonido y música mediante `SoundPlayer` y `MusicPlayer`
- UI completa en Material Design 3

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una arquitectura **MVVM ligera**:

UI (Activities + Fragments)

↓

ViewModel

↓

Repository

↓

Room (DAO – Entities – Database)

Esta separación mejora:
- Legibilidad del código
- Escalabilidad para añadir más funcionalidades
- Testabilidad de cada capa

---

## 📱 Pantallas de la aplicación

### 1. **MainActivity**
- Pantalla inicial
- Botón *Empezar*, acceso a Ayuda, Ajustes y Resultados

### 2. **SettingsActivity**
- Selector de idioma
- Activar/desactivar sonidos y música

### 3. **GameActivity + QuestionFragment**
- Muestra las preguntas dinámicamente
- Puede contener texto, imagen o imágenes como opciones
- Botón *Comprobar* y *Siguiente/Finalizar*

### 4. **ResultsActivity**
- Muestra los resultados finales de una partida
- Puntuación, aciertos, fallos, total

### 5. **Historial de partidas (RecyclerView)**
- Lista de partidas almacenadas localmente
- Implementada con `ResultsViewModel` + `GameResultAdapter`

### 6. **HelpActivity**
- Explicación interactiva del funcionamiento del juego

---

## 📌 Clases principales

### `BaseActivity`
Gestiona la Toolbar común y navegación superior.

### `GameRepository`
Proporciona acceso a la base de datos y genera preguntas desde recursos de strings.  
Encapsula:
- Obtención de preguntas
- Inserción de resultados
- Conversión entre entidades y modelos de UI

### `GameViewModel`
Mantiene el estado de la partida:
- Pregunta actual
- Opciones
- Puntuación acumulada

### `QuestionFragment`
Renderiza cada pregunta según su tipo:
- RadioButtons con texto
- Imagen superior
- Opciones visuales

### `AppPreferences`
Maneja:
- Idioma actual
- Sonidos
- Música

### Room:
- **Entities:** `Question`, `GameResult`
- **DAO:** `QuestionDao`, `GameResultDao`
- **Database:** `AppDatabase`

---

## 🗄️ Gestión de datos con Room

Room se utiliza de forma pedagógica para:
- Guardar las partidas (fecha, aciertos, fallos, puntuación)
- Guardar preguntas iniciales la primera vez que se ejecuta la app

En una app profesional, las preguntas se descargarían desde un servidor remoto.  
Aquí, Room permite demostrar:
- Relaciones 1→N
- Consultas con filtros
- Seeds iniciales

---

## 🌐 Cambio de idioma

El usuario puede alternar entre:
- **ES (español)**
- **EN (inglés)**

El cambio:
- Actualiza `Locale`
- Reinicia actividades necesarias
- Recarga strings desde `/values` o `/values-en`

Se almacenan en `SharedPreferences`.

---

## 📊 Diagrama 1: Arquitectura general

*POR HACER*

---

## 📴 Diagrama 2: Flujo de navegación


---

## 🧪 Entorno de pruebas

- **Android Studio Koala | 2024**
- **SDK mínimo:** 26
- **SDK objetivo:** 34
- **Emulador usado:** Pixel 6 – Android 14
- Theme: Material3 DayNight

---

## ⚙️ Dificultades y soluciones

### 1. **Conflictos de recursos XML (strings duplicados y escapes inválidos)**
✔ Depuración línea a línea  
✔ Eliminación de caracteres ilegales  
✔ Limpieza de compilación (`Build → Clean → Rebuild`)

### 2. **Cambio de idioma sin reiniciar correctamente**
✔ Aplicación de `LocaleHelper` custom  
✔ Recreación de actividades superiores (`recreate()`)

### 3. **Warning de accesibilidad en RadioButtons**
✔ Eliminación de textos duplicados accesibles (`android:contentDescription`)

### 4. **Diseño Material adaptable a tablets**
✔ Ancho máximo (`maxWidth="600dp"`)  
✔ Centrado automático según densidad

---

## 🎯 Conclusiones

- El proyecto cumple todos los requisitos de una app educativa y demostrativa.
- La arquitectura MVVM y el uso de Room aseguran escalabilidad.
- El tratamiento de recursos multilenguaje mejora la accesibilidad de la aplicación.
- El proyecto es fácilmente ampliable: nuevas preguntas, modos de juego, estadísticas avanzadas o incluso backend remoto.

---

## 📚 Bibliografía (APA7)

- Google. (2024). *Android Developers Documentation*. https://developer.android.com
- Google. (2024). *Guide to App Architecture*. https://developer.android.com/topic/architecture
- Material Design. (2024). *Material 3 Guidelines*. https://m3.material.io
- Fowler, M. (2002). *Patterns of Enterprise Application Architecture*. Addison-Wesley.


