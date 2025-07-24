# Levanamiento de Base de datos

``` docker 
docker run -d \
  --name postgres_container \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -e POSTGRES_DB=mi_base \
  -p 5432:5432 \
  postgres:15

```

# Levantameinto de Proyecto
``` docker 
docker run -d \
  --name kruger-backend \
  -p 8080:8080 \
  --env APP_NAME=kfullstack \
  --env SERVER_CONTEXT_PATH=/kfullstack \
  --env SWAGGER_UI_PATH=/swagger-ui.html \
  --env SWAGGER_UI_FILTER=true \
  --env SWAGGER_API_TITLE="Sudamericana de Software" \
  --env SWAGGER_API_DESCRIPTION="Microservicio de kfullstack" \
  --env SWAGGER_API_VERSION=1.0 \
  --env SWAGGER_API_EMAIL=info@sasf.net \
  --env SWAGGER_API_NAME="Sudamericana de Software" \
  --env SWAGGER_API_PAGEURL=www.sasf.net \
  --env SWAGGER_API_URI=http://localhost:8080/kfullstack/ \
  --env SWAGGER_API_DEV_URI=http://localhost:8080/kfullstack/ \
  --env LOG_LEVEL=DEBUG \
  --env SPRING_PROFILE=dev \
  --env DB_HOST=postgres \
  --env DB_PORT=5432 \
  --env DB_NAME=kfullstackdb \
  --env DB_USERNAME=kfullstackuser \
  --env DB_PASSWORD=kfullstackpass \
  --env JPA_DDL_AUTO=update \
  --env JPA_SHOW_SQL=false \
  --env JPA_FORMAT_SQL=true \
  --env SERVER_PORT=8080 \
  --env JWT_SECRET=SudamericanaDeSoftware1234567890123456 \
  --env JWT_EXPIRATION=86400000 \
  --network app-network \
  postgres:15
```


# Backend:

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