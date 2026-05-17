FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY certificate-service/target/*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
