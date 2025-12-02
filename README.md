# Memoria del Proyecto: Quiz Game

**Asignatura:** Desarrollo de Aplicaciones Móviles  
**Grado:** Grado en Ingeniería Informática  
**Curso:** 2054-2026  
**Autor:** Nicolás Vélez Leal

---

## Descripción General de la Aplicación
*Quiz Game* es una aplicación nativa Android diseñada para ofrecer una experiencia de juego tipo trivial dinámica y configurable. La app permite a los usuarios jugar partidas de 3 a 10 preguntas, soportando formatos multimedia (texto e imágenes) y ofreciendo retroalimentación auditiva inmediata. Destaca por su capacidad de cambiar el idioma (Español/Inglés) sin duplicidad de datos y por la persistencia del historial de partidas mediante una base de datos local SQLite gestionada con Room.

## Pantallas y Navegación
La navegación de la aplicación sigue un flujo lineal e intuitivo, diseñado bajo los principios de Material Design 3.

* **MainActivity:** Es el punto de entrada. Permite al usuario configurar la longitud de la partida mediante un diálogo modal y acceder a las pantallas secundarias de configuración y ayuda.
* **SettingsActivity:** Pantalla de gestión de preferencias persistentes. Aquí el usuario puede alternar el idioma de la aplicación y activar o desactivar los efectos de sonido y la música de fondo.
* **GameActivity:** El núcleo de la experiencia. Orquesta el flujo del juego, gestionando el tiempo, la puntuación y la navegación entre preguntas. Contiene al *QuestionFragment*.
* **ResultsActivity:** Se presenta al finalizar la partida. Muestra un resumen del rendimiento (puntuación, aciertos, fallos) y ofrece acceso al historial completo de partidas anteriores.
* **HelpActivity:** Proporciona una guía visual estática sobre las reglas y el funcionamiento de la aplicación.

**Relación entre pantallas:** Desde la *MainActivity* se puede navegar a cualquier configuración o iniciar el juego (*GameActivity*). Al concluir el juego, el flujo deriva automáticamente a *ResultsActivity*, desde donde se puede regresar al inicio, cerrando el ciclo de navegación.

## Descripción de Clases por Paquetes
A continuación se detalla la estructura del proyecto, organizada en paquetes lógicos que separan responsabilidades según la arquitectura MVVM.

### Paquete `com.example.practica2` (Raíz)
* **QuizApplication:** Punto de entrada de la aplicación a nivel de sistema. Gestiona el ciclo de vida global de los componentes multimedia (como pausar la música cuando la app pasa a segundo plano) y fuerza la inicialización temprana de la base de datos para evitar latencias en la primera partida.
* **AppPreferences:** Clase de utilidad que encapsula el acceso a `SharedPreferences`. Provee métodos estáticos para leer y escribir configuraciones de usuario persistentes, como el estado del sonido, la música y el idioma seleccionado.

### Paquete `com.example.practica2.ui.view` (Vistas)
* **MainActivity:** Actividad principal que gestiona el menú de inicio y la lógica del diálogo de selección de preguntas.
* **GameActivity:** Contenedor principal del juego. Observa los ViewModels para reaccionar a cambios de estado (fin de juego, actualización de puntuación) y coordina la navegación.
* **BaseActivity:** Clase abstracta de la que heredan todas las actividades. Centraliza la configuración de la *Toolbar* y el manejo de menús comunes, reduciendo la duplicidad de código en la interfaz.
* **QuestionFragment:** Fragmento reutilizable que renderiza la UI de una pregunta. Se adapta dinámicamente según el tipo de pregunta (Texto-Texto, Imagen-Texto, Texto-Imagen) e infla las vistas correspondientes.
* **ResultsActivity:** Pantalla final que presenta el resumen de la partida y aloja el `RecyclerView` con el historial.
* **SettingsActivity:** Actividad de ajustes que permite modificar el idioma y las preferencias de audio en tiempo real.
* **HelpActivity:** Actividad informativa estática que muestra las instrucciones de uso.

### Paquete `com.example.practica2.ui.viewmodel` (Lógica)
* **GameViewModel:** Gestiona el estado de una partida activa. Mantiene el índice de la pregunta actual, la puntuación y la validación de respuestas. Sobrevive a cambios de configuración como la rotación de pantalla.
* **ResultsViewModel:** Intermedia entre la UI de resultados y el repositorio. Ejecuta la inserción de la nueva partida y recupera la lista histórica de resultados de forma asíncrona.

### Paquete `com.example.practica2.ui.adapter`
* **GameResultAdapter:** Adaptador para el `RecyclerView` del historial. Vincula los datos de la lista de objetos `GameResult` con las vistas de cada fila (`item_game_result.xml`).

### Paquete `com.example.practica2.data.local` (Base de Datos)
* **AppDatabase:** Define la base de datos Room. Implementa el patrón Singleton. Una característica clave es que incluye un *Callback* en su método `onCreate` que contiene la lógica para poblar la base de datos con las preguntas iniciales (*Seed*) directamente, eliminando la necesidad de clases auxiliares externas.
* **QuestionDao y GameResultDao:** Interfaces de acceso a datos (DAO). Definen las consultas SQL abstraídas (inserciones, selecciones aleatorias y consultas ordenadas por fecha).

### Paquete `com.example.practica2.data.model` (Entidades)
* **Question:** Entidad que representa una pregunta. Almacena identificadores de recursos (`int`) en lugar de texto para facilitar la internacionalización.
* **GameResult:** Entidad que modela el resultado de una partida (puntuación, fecha, aciertos/fallos) para su persistencia en la tabla histórica.

### Paquete `com.example.practica2.repository`
* **GameRepository:** Actúa como *Single Source of Truth*. Coordina las operaciones de datos entre los ViewModels y la base de datos local, ejecutando las tareas pesadas en hilos secundarios mediante `ExecutorService`.

### Paquete `com.example.practica2.media`
* **SoundPlayer y MusicPlayer:** Clases *wrapper* que encapsulan `SoundPool` y `MediaPlayer`. Simplifican la reproducción de efectos y música, manejando internamente la liberación de recursos y el respeto a las preferencias del usuario.

## Entorno de Pruebas y Requisitos
Las pruebas funcionales de la aplicación se han realizado utilizando el emulador oficial de Android Studio (AVD).
* **Dispositivo emulado:** Medium Phone
* **Versión de Android:** API 36 (Android 16 Preview)
* **Min SDK:** API 29 (Android 10)
* **Resolución:** 1080 x 2400

## Dificultades y Soluciones

**Condición de Carrera en la Base de Datos**
* *Dificultad:* Al iniciar la aplicación por primera vez, la consulta de preguntas se ejecutaba antes de que la inserción de datos iniciales finalizara, resultando en una pantalla en blanco.
* *Solución:* Se integró la lógica de inserción (*seeding*) dentro del `RoomDatabase.Callback` en la clase `AppDatabase`, asegurando que los datos estén disponibles desde la creación de la base de datos.

**Internacionalización con Persistencia**
* *Dificultad:* Guardar texto estático en la BD impedía el cambio de idioma dinámico.
* *Solución:* Se refactorizó el modelo `Question` para almacenar IDs de recursos (`R.string...`). La vista resuelve el texto en tiempo de ejecución según el idioma del dispositivo.

## Conclusiones
El desarrollo de *Quiz Game* ha permitido consolidar los conceptos fundamentales de la arquitectura Android moderna. La estructuración en paquetes lógicos (UI, Data, Domain) junto con MVVM facilita la mantenibilidad. La integración de la carga de datos directamente en la base de datos ha simplificado el flujo de inicialización, resultando en una aplicación robusta y eficiente.

---
### Referencias
* Google. (2024). Guide to app architecture. Android Developers.
* Google. (2024). Save data in a local database using Room. Android Developers.
* Fowler, M. (2002). Patterns of Enterprise Application Architecture. Addison-Wesley Professional.
* Material Design. (2024). Material 3 Guidelines.