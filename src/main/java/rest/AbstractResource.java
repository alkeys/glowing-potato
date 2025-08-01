package rest;

import control.GenericDAO;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractResource<T, ID> {

    protected abstract GenericDAO<T, ID> getDAO();
    protected abstract Logger getLogger();

    @GET
    @Path("/listar")
    public Response listar() {
        try {
            List<T> lista = getDAO().findAll();
            return Response.ok(lista).build();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al listar elementos", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar elementos\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    public Response listarPaginado(@QueryParam("start") @DefaultValue("0") int start,
                                   @QueryParam("size") @DefaultValue("50") int size) {
        try {
            if (start < 0 || size <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Parámetros inválidos\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            List<T> lista = getDAO().findRange(start, size);
            int total = getDAO().count();

            return Response.ok(lista)
                    .header("Total-Records", total)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al listar elementos por rango", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al listar elementos\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") ID id) {
        try {
            T entity = getDAO().findById(id);
            if (entity != null) {
                return Response.ok(entity).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Elemento no encontrado con ID: " + id + "\"}")
                        .build();
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al obtener por ID", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al obtener por ID\"}")
                    .build();
        }
    }

    @POST
    @Path("/crear")
    @Transactional
    public Response crear(T entity, @Context UriInfo uriInfo) {
        try {
            getDAO().create(entity);
            Object id = getDAO().getId(entity);

            if (id != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(id.toString());
                return Response.created(uriBuilder.build())
                        .entity(entity)
                        .build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Entidad no fue persistida correctamente (ID nulo).\"}")
                        .build();
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al crear entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al crear entidad\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response actualizar(@PathParam("id") ID id, T entity) {
        try {
            T existente = getDAO().findById(id);
            if (existente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No se encontró entidad con ID: " + id + "\"}")
                        .build();
            }

            getDAO().setId(entity, id); // para que el ID del objeto sea consistente
            getDAO().update(entity);
            return Response.ok(entity).build();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al actualizar entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al actualizar entidad\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response eliminar(@PathParam("id") ID id) {
        try {
            T entity = getDAO().findById(id);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Entidad no encontrada con ID: " + id + "\"}")
                        .build();
            }

            getDAO().delete(entity);
            return Response.noContent().build();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error al eliminar entidad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno al eliminar entidad\"}")
                    .build();
        }
    }
}
