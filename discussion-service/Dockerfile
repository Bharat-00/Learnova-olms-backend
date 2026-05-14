FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/discussion-service-0.0.1-SNAPSHOT.jar discussion-service.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "discussion-service.jar"]