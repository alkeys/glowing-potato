package rest;

import control.AbstractDataPersistence;
import control.CitaDAO;
import entity.Cita;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path("/citas") // La ruta base se define aquí, en la clase concreta
public class CitaResource extends AbstractCrudResource<Cita, Integer> {

    @Inject
    private CitaDAO citaDAO;

    @Override
    protected AbstractDataPersistence<Cita> getService() {
        return citaDAO;
    }

    @Override
    protected Integer getId(Cita entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Cita entity, Integer id) {
        entity.setId(id);
    }

    // ¡Y eso es todo!
    // Los endpoints /listar, /obtener/{id}, /crear, etc., son heredados.
    // Si necesitas un endpoint específico para Cita que no sea CRUD,
    // lo puedes agregar aquí.
}