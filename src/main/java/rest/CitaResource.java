package rest;

import control.CitaDAO;
import entity.Cita;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    @Inject
    private CitaDAO citaDAO;

    private static final Logger LOG = Logger.getLogger(CitaResource.class.getName());

    // Listar todas las citas
    @GET
    @Path("/listar")
    public Response listar() {
        try {
            List<Cita> citas = new ArrayList<>(citaDAO.findAll());
            return Response.ok(citas).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar citas", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar citas\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Listar citas con paginación
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

            List<Cita> citas = new ArrayList<>(citaDAO.findRange(start, size));
            int total = citaDAO.count();

            return Response.ok(citas)
                    .header("Total-Records", total)
                    .build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar citas por rango", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar citas\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Obtener por ID
    @GET
    @Path("/obtener/{id}")
    public Response obtenerPorId(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID no puede ser nulo\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            Cita cita = citaDAO.findById(id);
            if (cita != null) {
                return Response.ok(cita).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró una cita con id: " + id + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener cita por id", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al obtener cita\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Crear cita
    @POST
    @Transactional
    @Path("/crear")
    public Response crearCita(Cita cita, @Context UriInfo uriInfo) {
        if (cita == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La cita no puede ser nula\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            citaDAO.create(cita);

            if (cita.getId() != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(cita.getId().toString());
                return Response.created(uriBuilder.build())
                        .entity(cita)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"La cita no fue persistida correctamente\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al crear cita", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear la cita\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Actualizar cita
    @PUT
    @Path("/actualizar/{id}")
    @Transactional
    public Response actualizarCita(@PathParam("id") Integer id, Cita cita) {
        if (id == null || cita == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID y cita no pueden ser nulos\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            Cita existente = citaDAO.findById(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró cita con id: " + id + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            cita.setId(id);
            citaDAO.update(cita);
            return Response.ok(cita).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar cita", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al actualizar cita\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // Eliminar cita
    @DELETE
    @Path("/Eliminar/{id}")
    @Transactional
    public Response eliminarCita(@PathParam("id") Integer id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID no puede ser nulo\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            Cita cita = citaDAO.findById(id);
            if (cita == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró cita con id: " + id + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            citaDAO.delete(cita);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar cita", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al eliminar cita\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
