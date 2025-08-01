package rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class Mascotarest extends Application {

    // No es necesario implementar métodos adicionales aquí,
    // ya que la clase ApplicationPath ya configura el contexto base de la aplicación REST.
    // Los recursos REST se definirán en otras clases anotadas con @Path.

}

