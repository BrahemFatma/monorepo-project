FROM openjdk:17

WORKDIR /app

COPY target/users-service.jar users-service.jar

ENTRYPOINT ["java", "-jar", "users-service.jar"]

EXPOSE 8090
