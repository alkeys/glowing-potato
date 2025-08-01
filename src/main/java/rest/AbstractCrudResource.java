package rest;

import control.AbstractDataPersistence;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Recurso REST abstracto y genérico para operaciones CRUD.
 * @param <T>  El tipo de la entidad (ej. Cita, Mascota).
 * @param <ID> El tipo del ID de la entidad (ej. Integer, Long).
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractCrudResource<T, ID> {

    private static final Logger LOG = Logger.getLogger(AbstractCrudResource.class.getName());

    /**
     * Método abstracto que las clases hijas deben implementar
     * para proporcionar el DAO (servicio de persistencia) específico.
     * @return Una instancia de AbstractDataPersistence para la entidad T.
     */
    protected abstract AbstractDataPersistence<T> getService();

    /**
     * Método abstracto para obtener el ID de una entidad.
     * @param entity la entidad.
     * @return el ID de la entidad.
     */
    protected abstract ID getId(T entity);

    /**
     * Método abstracto para establecer el ID en una entidad.
     * Se usa principalmente en la actualización.
     * @param entity la entidad.
     * @param id el id a establecer.
     */
    protected abstract void setId(T entity, ID id);

    @GET
    @Path("/listar")
    public Response listar() {
        try {
            List<T> entities = new ArrayList<>(getService().findAll());
            return Response.ok(entities).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar entidades", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar entidades\"}")
                    .build();
        }
    }

    @GET
    @Path("/obtener/{id}")
    public Response obtenerPorId(@PathParam("id") ID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID no puede ser nulo\"}")
                    .build();
        }
        try {
            T entity = getService().findById(id);
            if (entity != null) {
                return Response.ok(entity).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró una entidad con id: " + id + "\"}")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener entidad por id", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al obtener la entidad\"}")
                    .build();
        }
    }

    @POST
    @Path("/crear")
    @Transactional
    public Response crear(T entity, @Context UriInfo uriInfo) {
        if (entity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La entidad no puede ser nula\"}")
                    .build();
        }
        try {
            getService().create(entity);
            ID entityId = getId(entity);

            if (entityId != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(entityId.toString());
                return Response.created(uriBuilder.build()).entity(entity).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"La entidad no fue persistida correctamente\"}")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al crear la entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear la entidad\"}")
                    .build();
        }
    }

    @PUT
    @Path("/actualizar/{id}")
    @Transactional
    public Response actualizar(@PathParam("id") ID id, T entity) {
        if (id == null || entity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID y entidad no pueden ser nulos\"}")
                    .build();
        }
        try {
            if (getService().findById(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró entidad con id: " + id + "\"}")
                        .build();
            }
            setId(entity, id); // Aseguramos que la entidad tenga el ID correcto
            T updatedEntity = getService().update(entity);
            return Response.ok(updatedEntity).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al actualizar la entidad\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/eliminar/{id}") // Corregido a minúscula para consistencia
    @Transactional
    public Response eliminar(@PathParam("id") ID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID no puede ser nulo\"}")
                    .build();
        }
        try {
            T entity = getService().findById(id);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró entidad con id: " + id + "\"}")
                        .build();
            }
            getService().delete(entity);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar la entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al eliminar la entidad\"}")
                    .build();
        }
    }

    // El método de paginación también se puede generalizar
    @GET
    @Path("/listar/rango")
    public Response listarPorRango(@QueryParam("start") @DefaultValue("0") int start,
                                   @QueryParam("size") @DefaultValue("50") int size) {
        try {
            if (start < 0 || size <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Parámetros de paginación inválidos\"}")
                        .build();
            }
            List<T> entities = new ArrayList<>(getService().findRange(start, size));
            int total = getService().count();
            return Response.ok(entities).header("Total-Records", total).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar entidades por rango", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar por rango\"}")
                    .build();
        }
    }
}