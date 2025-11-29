# Utilisation de l'image OpenJDK 17
FROM openjdk:17

# Définition du répertoire de travail
WORKDIR /app

# Copie du fichier JAR généré dans l'image
COPY target/users-service.jar users-service.jar

# Définition de la commande de démarrage du service
ENTRYPOINT ["java", "-jar", "users-service.jar"]

# Exposition du port de l'application (doit correspondre à celui du `docker-compose.yml`)
EXPOSE 8090
