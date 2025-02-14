# Etapa de construcción
FROM eclipse-temurin:17-jdk as build

# Copiar los archivos del proyecto
COPY . /app
WORKDIR /app

# Dar permisos de ejecución a mvnw y construir el proyecto
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Copiar el archivo .jar construido
RUN mv -f target/*.jar app.jar

# Etapa de ejecución
FROM eclipse-temurin:17-jre

# Declarar el puerto como variable de entorno
ARG PORT
ENV PORT=${PORT}

# Copiar el .jar desde la etapa de construcción
COPY --from=build /app/app.jar .

# Crear un usuario de ejecución
RUN useradd runtime
USER runtime

# Definir el punto de entrada
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
