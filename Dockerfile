# Use Open Liberty with Java 21
# Compile the application with Java 21 using Maven
# mvn clean package
#despues de ejecutar el comando mvn clean package
#construir la imagen con el siguiente comando
#docker build -t veterinaria .
#correr la imagen con el siguiente comando se ocupa el puerto 9080 como puerto de entrada al contenedor y el puerto 10000 como puerto de salida del contenedor
#docker run -p 9080:10000  veterinaria
#imagen base de open liberty con java 21
FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi-minimal

# Copia el archivo server.xml a la carpeta /config del contenedor para asi poder configurar el servidor de open liberty con la base de datos postgresql
COPY --chown=1001:0 src/main/liberty/config/server.xml /config/

# Copia el driver de la base de datos a la carpeta /config
COPY --chown=1001:0 src/main/liberty/config/postgresql-42.7.4.jar /config/

# Copia el archivo WAR al directorio de aplicaciones de OpenLiberty
COPY --chown=1001:0 target/cine-1.0-SNAPSHOT.war /config/dropins/

# Copia el archivo keystore al contenedor
COPY --chown=1001:0  /keystore.p12 /config/

# Exponer puertos (Opcional para el desarrollo local)
EXPOSE 9080 9443  10000


# Inicia el servidor de OpenLiberty usando la variable de entorno PORT de Render
CMD ["server", "run", "defaultServer"]