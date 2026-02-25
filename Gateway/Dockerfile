# Etap 1: Budowanie
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: Uruchamianie
FROM openjdk:17-jdk-slim
WORKDIR /app
# Zauważ zmianę nazwy pliku JAR na Gateway
COPY --from=build /app/target/Gateway-0.0.1-SNAPSHOT.jar app.jar

# Gateway działa na porcie 8085
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]