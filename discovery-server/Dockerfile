FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY discovery-server/target/*.jar discovery-server.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "discovery-server.jar"]
