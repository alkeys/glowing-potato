package rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class Mascotarest extends Application {

    // This class is intentionally left empty.
    // It serves as a configuration class for JAX-RS to define the base URI for RESTful web services.
    // The base URI for all RESTful web services will be prefixed with "/v1".
    //http://192.168.1.7:9090/mascotas-1.0-SNAPSHOT/api/v1/hello-world/

}

