FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/discovery-server-0.0.1-SNAPSHOT.jar discovery-server.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "discovery-server.jar"]