# LOVECODE

## Estructura del Proyecto

El repositorio de **LoveCode** está organizado siguiendo una estructura limpia que separa la lógica del servidor, la interfaz de usuario y la persistencia de datos:

### 1. Backend (`/backend`)
Contiene la lógica de negocio desarrollada en **Java 21** y la gestión de dependencias con **Maven**:
* **`src/main/java/com/LoveCode/`**:
    * `Main.java`: Punto de entrada de la aplicación. Gestiona el flujo del programa, la entrada de datos por consola y las operaciones de inserción y consulta de usuarios.
    * `ConexionDB.java`: Módulo técnico que establece la conexión con el servidor MariaDB (IP: 192.168.204.129) mediante JDBC.
* **`pom.xml`**: Archivo de configuración de Maven que automatiza la descarga de librerías como el driver de MariaDB y el sistema de logs.

### 2. Frontend (`/frontend`)
Recursos del lenguaje de marcas para la interfaz visual:
* `index.html` / `index.css`: Página de inicio con diseño de perfiles y estética *Dark Mode*.
* `login.html` / `register.html`: Formularios de acceso y creación de cuenta con campos para nombre, email, ciudad y descripción.
* `logo_lovecode.png`: Logo

### 3. Database (`/database`)
Recursos de persistencia y administración del sistema:
* **`lovecode_db-backup.sql`**: Script de respaldo que contiene la estructura completa de las tablas (`Usuarios`, `Tecnologias`, `Likes`, `Matches`) y datos de prueba para replicar el entorno.

---
