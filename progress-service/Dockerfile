FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/progress-service-0.0.1-SNAPSHOT.jar progress-service.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "progress-service.jar"]