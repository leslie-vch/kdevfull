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