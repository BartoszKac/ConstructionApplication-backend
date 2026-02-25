# Etap 1: Budowanie aplikacji
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: Uruchamianie aplikacji
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Upewnij się, że masz tu "="
COPY --from=build /app/target/EurekaSerwer-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]