package rest;


import control.UsuariosDAO;
import entity.Cita;
import entity.Usuario;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {


    @Inject
    private UsuariosDAO usuariosDAO;

    private static final Logger LOG = Logger.getLogger(UsuarioResource.class.getName());



    // Endpoint para logiarse de usuario
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username y password no pueden ser nulos.")
                    .build();
        }

        try {
            Usuario usuario = usuariosDAO.login(username, password);
            if (usuario != null) {
                return Response.ok(usuario).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciales inválidas.")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al iniciar sesión", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno al iniciar sesión.")
                    .build();
        }
    }



    //para listar todas las citas con paginación
    @GET
    @Path("/listar")
    public List<Usuario> listar() {
        try {
            return usuariosDAO.findAll();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar citas", e);
            throw new WebApplicationException("Error interno al listar citas.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/listar/rango")
    public Response listarCitas(@QueryParam("start") @DefaultValue("0") int start,
                                @QueryParam("size") @DefaultValue("50") int size) {
        try {
            if (start < 0 || size <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Parámetros inválidos\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            List<Usuario> lista = usuariosDAO.findRange(start, size);
            int total = usuariosDAO.count();

            return Response.ok(lista)
                    .header("Total-Records", total)
                    .type(MediaType.APPLICATION_JSON) // 👈 asegura que sea JSON
                    .build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar citas por rango", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar citas\"}")
                    .type(MediaType.APPLICATION_JSON) // 👈 asegura que sea JSON
                    .build();
        }
    }



    @GET
    @Path("/obtener/{id}")
    public Response obtenerPorId(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID no puede ser nulo.")
                    .build();
        }

        try {
            Usuario  obj = usuariosDAO.findById(id);
            if (obj != null) {
                return Response.ok(obj).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró una cita con id: " + id)
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener cita por id", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno al obtener cita.")
                    .build();
        }
    }


    @POST
    @Transactional
    @Path("/crear")
    public Response crearResponse(Usuario obj, @Context UriInfo uriInfo) {
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El usuario no puede ser nulo\"}")
                    .build();
        }

        try {
            usuariosDAO.create(obj);
            System.out.println("ID generado: " + obj.getId()); // 👈 prueba real


            if (obj.getId() != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(obj.getId().toString());
                return Response.created(uriBuilder.build())
                        .entity(obj)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"El usuario no fue persistido correctamente (ID nulo).\"}")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al crear usuario", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear usuario: " + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }


    @PUT
    @Path("/actualizar/{id}")
    @Transactional
    public Response actualizarResponse(@PathParam("id") Integer id, Usuario obj) {
        if (id == null ||  obj == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID y cita no pueden ser nulos.")
                    .build();
        }

        try {
             Usuario existente = usuariosDAO.findById(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró cita con id: " + id)
                        .build();
            }

             obj.setId(id); // asegurarse que el ID sea el correcto
            usuariosDAO.update( obj);

            return Response.ok( obj).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar cita", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno al actualizar cita.")
                    .build();
        }
    }

    @DELETE
    @Path("/Eliminar/{id}")
    @Transactional
    public Response eliminarResponse(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID no puede ser nulo.")
                    .build();
        }

        try {
            Usuario obj = usuariosDAO.findById(id);
            if (obj == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontró Usuario con id: " + id)
                        .build();
            }

            usuariosDAO.delete(obj);
            return Response.ok("Usuario eliminado correctamente.").build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar cita", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error interno al eliminar cita.")
                    .build();
        }
    }
}