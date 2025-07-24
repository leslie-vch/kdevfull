# Proyecto Kruger (Backend, Frontend y PostgreSQL)

Este proyecto consta de tres servicios que se despliegan utilizando Docker Compose:  
1. **PostgreSQL**: Base de datos para almacenar los datos de la aplicación.  
2. **Backend**: Microservicio desarrollado en Spring Boot.  
3. **Frontend**: Aplicación frontend construida con un framework de tu elección (React, Angular, etc.).

## Requisitos

- Docker: Asegúrate de tener Docker y Docker Compose instalados en tu máquina. Si no tienes Docker, puedes instalarlo desde [docker.com](https://www.docker.com/get-started).

## Despliegue

### 1. Clona este repositorio

```bash
git clone <URL_DE_TU_REPOSITORIO>
cd <NOMBRE_DEL_REPOSITORIO>

docker-compose up   -d

```

# Verificación de los servicios

Una vez que los contenedores estén levantados, puedes verificar lo siguiente:

PostgreSQL estará disponible en el puerto 5432 (por defecto, sin necesidad de credenciales especiales).

Backend estará disponible en el puerto 8080, con la ruta base de la API en http://localhost:8080/kfullstack/.

Frontend estará disponible en el puerto 3000, y puedes acceder a la aplicación web a través de http://localhost:3000/.

# Acceso a Swagger UI (para la API)
Puedes acceder a la documentación interactiva de Swagger UI en la siguiente URL:

http://localhost:8080/kfullstack/swagger-ui.html

# Variables de entorno
A continuación, se presentan las variables de entorno utilizadas en la configuración:

## Backend:

- APP_NAME: Nombre de la aplicación.

- SERVER_CONTEXT_PATH: Ruta base para las API.

-  SWAGGER_UI_PATH: Ruta para acceder a la documentación de la API.

- DB_HOST: Dirección del host de la base de datos PostgreSQL.

- DB_PORT: Puerto de la base de datos PostgreSQL.

- DB_NAME: Nombre de la base de datos.

- DB_USERNAME: Usuario de la base de datos.

- DB_PASSWORD: Contraseña de la base de datos.

- JWT_SECRET: Clave secreta para la autenticación JWT.

- JWT_EXPIRATION: Tiempo de expiración del token JWT (en milisegundos).

## Frontend:

Depende de que el backend esté disponible en el contenedor para realizar llamadas API.
