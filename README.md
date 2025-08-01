
# Servidor de Desarrollo de Liberty
Este proyecto utiliza el plugin de Maven para Open Liberty, que permite ejecutar un servidor de desarrollo de Liberty. 
El servidor se ejecuta en modo de desarrollo, lo que significa que los cambios en el código fuente se reflejarán automáticamente
sin necesidad de reiniciar el servidor.

# Requisitos
- Java 21 
- Maven 3.8.6 o superior
- Open Liberty
- Docker (opcional, para ejecutar el servidor en un contenedor) 
- base de datos PostgreSQL (opcional, para la persistencia de datos) 

# Instrucciones de Uso y Configuración
Para ejecutar el servidor de desarrollo de Liberty, asegúrate de tener Java ,base de datos y maven  instalados en tu sistema.
Luego, puedes utilizar el siguiente comando para iniciar el servidor:

```bash
./mvnw clean install
```
Para ejecutar el servidor de desarrollo de Liberty, puedes utilizar el siguiente comando:

```bash
./mvnw io.openliberty.tools:liberty-maven-plugin:dev
```

# Configuración de la Base de Datos se recomienda utilizar PostgreSQL en docker para mayor facilidad de uso. en local
Para configurar la base de datos PostgreSQL, puedes utilizar el siguiente comando para ejecutar un contenedor de Docker:
credenciales de acceso a la base de datos:
- **Base de datos**: mascotas
- **Usuario**: admin
- **Contraseña**: admin
- Puerto: 5432
- Para Crear el contenedor de PostgreSQL, asegúrate de tener Docker instalado y ejecuta el siguiente comando:
```bash
docker run --name mascotas -e POSTGRES_DB=mascotas -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -p 5432:5432 -d postgres
```
#para correr el contenedor de PostgreSQL, asegúrate de tener Docker instalado y ejecuta el siguiente comando:
```bash
docker start mascotas
```

# Creación de contendor de Docker para el Servidor de Desarrollo de Liberty 
Para crear un contenedor de Docker para el servidor de desarrollo de Liberty, puedes utilizar el siguiente comando:

```bash
docker build -t veterinaria .
```

# Ejecución del Contenedor de Docker
Para ejecutar el contenedor de Docker que has creado, utiliza el siguiente comando:

```bash 
docker run -p 9090:9090 -p 9443:9443 veterinaria
```
