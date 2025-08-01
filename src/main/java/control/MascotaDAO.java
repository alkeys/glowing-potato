package control;


import entity.Mascota;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class MascotaDAO extends AbstractDataPersistence<Mascota> {

    @PersistenceContext(unitName = "JPA")
    private EntityManager em;

    public MascotaDAO() {
        super(Mascota.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }



}
