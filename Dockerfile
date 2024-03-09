# Imagen base de la cual se va a partir para crear la imagen de la aplicación
FROM openjdk:17-jdk-alpine
# Copiamos el jar generado en la carpeta target
COPY target/Back_Pasteurizadora-0.0.1-SNAPSHOT.jar java-app.jar
# Comando que se ejecutará al iniciar el contenedor
ENTRYPOINT ["java","-jar","/java-app.jar"]