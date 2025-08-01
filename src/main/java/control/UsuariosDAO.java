package control;

import entity.Usuario;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;


@Stateless
@LocalBean
public class UsuariosDAO extends AbstractDataPersistence<Usuario> implements Serializable {

    @PersistenceContext(unitName = "JPA")
    private EntityManager em;

    public UsuariosDAO() {
        super(Usuario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Usuario login(String username, String password) {
        try {
            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nombre = :username AND u.contrasena = :password", Usuario.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getResultList();

            return usuarios.isEmpty() ? null : usuarios.get(0);

        } catch (Exception e) {
            // Si no se encuentra el usuario, se retorna null
            return null;
        }
    }
}
