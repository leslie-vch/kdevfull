# Etapa de construcción
FROM openjdk:17-slim AS build

# Instalamos Maven en la imagen de OpenJDK 17
RUN apt-get update && apt-get install -y maven

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Construye el archivo JAR
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-slim

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 8080 para la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
