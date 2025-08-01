package control;

import entity.Cita;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;



@Stateless
@LocalBean
public class CitaDAO extends AbstractDataPersistence<Cita> implements Serializable {

    @PersistenceContext(unitName = "JPA")
    private EntityManager em;

    public CitaDAO() {
        super(Cita.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}


