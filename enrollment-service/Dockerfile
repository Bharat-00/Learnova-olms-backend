FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/enrollment-service-0.0.1-SNAPSHOT.jar enrollment-service.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "enrollment-service.jar"]