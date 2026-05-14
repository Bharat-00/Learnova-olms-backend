FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/payment-service-0.0.1-SNAPSHOT.jar payment-service.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "payment-service.jar"]