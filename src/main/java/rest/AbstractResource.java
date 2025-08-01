package rest;

import control.DataAccessObject;
import entity.BaseEntity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase base abstracta para los recursos REST que manejan operaciones CRUD.
 * @param <T> La entidad, que debe implementar BaseEntity.
 * @param <K> El tipo de la clave primaria (ID).
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractResource<T extends BaseEntity<K>, K> {

    private final Logger LOG;

    // Las clases hijas deben proporcionar el DAO específico.
    protected abstract DataAccessObject<T, K> getDAO();

    // El tipo de la entidad, para mensajes de error genéricos.
    private final Class<T> entityClass;

    public AbstractResource(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.LOG = Logger.getLogger(getClass().getName());
    }

    @POST
    @Transactional
    public Response create(T entity, @Context UriInfo uriInfo) {
        if (entity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La entidad no puede ser nula.\"}")
                    .build();
        }
        try {
            getDAO().create(entity);
            // Usamos la interfaz BaseEntity para obtener el ID
            if (entity.getId() != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(entity.getId().toString());
                return Response.created(uriBuilder.build()).entity(entity).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"La entidad no fue persistida correctamente (ID nulo).\"}")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al crear entidad " + entityClass.getSimpleName(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear la entidad.\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") K id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"El ID no puede ser nulo.\"}").build();
        }
        try {
            T entity = getDAO().findById(id);
            if (entity != null) {
                return Response.ok(entity).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró " + entityClass.getSimpleName() + " con id: " + id + "\"}")
                        .build();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar " + entityClass.getSimpleName() + " por id", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al buscar la entidad.\"}")
                    .build();
        }
    }

    @GET
    public Response findRange(@QueryParam("start") @DefaultValue("0") int start,
                              @QueryParam("size") @DefaultValue("20") int size) {
        try {
            List<T> result = getDAO().findRange(start, size);
            int total = getDAO().count();
            return Response.ok(result).header("Total-Records", total).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al listar " + entityClass.getSimpleName() + " por rango", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar entidades.\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") K id, T entity) {
        if (id == null || entity == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"El ID y la entidad no pueden ser nulos.\"}").build();
        }
        try {
            if (getDAO().findById(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró " + entityClass.getSimpleName() + " con id: " + id + "\"}")
                        .build();
            }
            entity.setId(id); // Aseguramos que el ID sea el correcto
            getDAO().update(entity);
            return Response.ok(entity).build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar " + entityClass.getSimpleName(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al actualizar la entidad.\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") K id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"El ID no puede ser nulo.\"}").build();
        }
        try {
            T entity = getDAO().findById(id);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró " + entityClass.getSimpleName() + " con id: " + id + "\"}")
                        .build();
            }
            getDAO().delete(entity);
            return Response.noContent().build(); // 204 No Content es estándar para DELETE exitoso
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar " + entityClass.getSimpleName(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al eliminar la entidad.\"}")
                    .build();
        }
    }
}