package rest;


import control.AbstractDataPersistence;
import control.MascotaDAO;
import entity.Mascota;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mascotas") // La ruta base se define aquí, en la clase concreta
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MascotasResource  extends AbstractCrudResource<Mascota, Integer> {

    // Aquí inyectamos el DAO específico para Mascota
    @Inject
    private MascotaDAO mascotasDAO;

    @Override
    protected AbstractDataPersistence<Mascota> getService() {
        return mascotasDAO;
    }

    @Override
    protected Integer getId(Mascota entity) {
        return entity.getId();
    }

    @Override
    protected void setId(Mascota entity, Integer id) {
        entity.setId(id);
    }


}
