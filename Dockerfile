# Etapa de construcción (Build stage)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias (aprovechando la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y empaquetamos la aplicación
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run stage)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el jar generado desde la etapa de construcción
COPY --from=build /app/target/price-service-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
