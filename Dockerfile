# Usamos una imagen base de Java 17
FROM maven:3.9.11-eclipse-temurin-25-alpine AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Copia el JAR generado al contenedor
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone el contenedor
EXPOSE 8080

# Comando de ejecución al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "/app/app.jar"]