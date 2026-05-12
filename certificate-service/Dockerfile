FROM eclipse-temurin:17

WORKDIR /app

COPY target/certificate-service.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java","-jar","app.jar"]