# LOVECODE

## Descripción

LoveCode es una aplicación de citas tecnológicas que combina una interfaz web frontend con un backend en Java y Spring Boot. Permite el registro e inicio de sesión de usuarios, gestión de tecnologías favoritas, envío de likes y visualización de matches.

## Estructura del Proyecto

### 1. Backend (`/backend`)

El backend está construido con **Java 21**, **Spring Boot 3.2.4** y **Maven**.

* `src/main/java/com/LoveCode/`
  * `Main.java`: Punto de entrada de la aplicación Spring Boot.
  * `ConexionDB.java`: Configura la conexión JDBC a MariaDB con credenciales locales.
  * `config/SecurityConfig.java`: Configura Spring Security para permitir todas las peticiones y usar BCrypt para el hash de contraseñas.
  * `controller/`:
    * `AuthController.java`: Registro de usuarios y login con verificación de contraseña BCrypt.
    * `UsuarioController.java`: Listado de usuarios y filtros para el dashboard.
    * `TecnologiaController.java`: Listado de tecnologías disponibles.
    * `LikeController.java`: Registro y eliminación de likes, y listado de likes dados.
    * `MatchController.java`: Listado de matches de un usuario.
  * `dao/`: Implementa el acceso a datos para usuarios, tecnologías, likes y matches.
  * `Usuario.java`: Modelo de datos para usuarios con campos como nombre, email, ciudad, descripción y tecnologías seleccionadas.

* `pom.xml`: Dependencias de Spring Boot, MariaDB y JUnit para pruebas.

### 2. Frontend (`/frontend`)

Interfaz web estática construida con HTML, CSS y JavaScript.

* `index.html` / `index.css`: Página principal y diseño de perfil.
* `login.html` / `login.css`: Formulario de inicio de sesión.
* `register.html` / `register.css`: Formulario de registro de nuevos usuarios.
* `dashboard.html` / `dashboard.css`: Panel de usuarios, likes y matches.
* `Auth.js`, `dashboard.js`: Lógica cliente para comunicación con el backend y renderizado dinámico.

### 3. Base de datos (`/backend/database`)

* `backup_lovecode_12-05-26.sql`
* `BackUp_LoveCode_db_19-05-2026_con_triggers_y_procedimientos.sql`
* `Backup_lovecode_db_26-05-2026_Final.sql`
* `lovecode_db-backup.sql`

Estos archivos contienen la estructura de tablas y datos de prueba para MariaDB.

## Características principales

* Registro de usuarios con contraseña encriptada con BCrypt.
* Inicio de sesión y validación de credenciales.
* Gestión de tecnologías por usuario.
* Envío de likes entre usuarios.
* Detección y listado de matches.
* API REST integrada con frontend estático.

## Requisitos

* Java 21
* Maven
* MariaDB

## Ejecución del backend

1. Abre una terminal en la carpeta `backend`.
2. Compila el proyecto:

```bash
mvn clean install
```

3. Ejecuta la aplicación:

```bash
mvn spring-boot:run
```

4. Accede al backend en:

```text
http://localhost:8080/api/
```

> Nota: La conexión a la base de datos se configura en `backend/src/main/java/com/LoveCode/ConexionDB.java`.

## Uso del frontend

1. Abre `frontend/index.html` en tu navegador.
2. Utiliza `login.html` para iniciar sesión.
3. Regístrate con `register.html` para crear una nueva cuenta.
4. Accede al dashboard con `dashboard.html` para ver usuarios, likes y matches.

## Endpoints principales

* `POST /api/auth/registro`
* `POST /api/auth/login`
* `GET /api/usuarios`
* `GET /api/tecnologias`
* `POST /api/likes`
* `DELETE /api/likes`
* `GET /api/likes/dados/{idUsuario}`
* `GET /api/matches/{idUsuario}`

## Notas

* El backend permite todas las peticiones HTTP sin autenticación adicional para facilitar el desarrollo.
* Para producción, sería recomendable asegurar los endpoints, externalizar la configuración de la base de datos y proteger las credenciales.
