FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/assesment-service-0.0.1-SNAPSHOT.jar assesment-service.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "assesment-service.jar"]