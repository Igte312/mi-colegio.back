# 1. Usamos una imagen base de Maven para compilar el proyecto
FROM maven:3.9.3-eclipse-temurin-17 AS build

# 2. Directorio de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiamos los archivos de Maven
COPY pom.xml .
COPY src ./src

# 4. Compilamos el proyecto y empaquetamos en un JAR
RUN mvn clean package -DskipTests

# 5. Usamos una imagen más ligera solo para correr el JAR
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# 6. Copiamos el JAR generado desde la etapa build
COPY --from=build /app/target/mi-colegio-1.0-SNAPSHOT.jar app.jar

# 7. Exponemos el puerto
EXPOSE 8080

# 8. Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
