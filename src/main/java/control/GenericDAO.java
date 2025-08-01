package control;

import jakarta.persistence.EntityManager;

import java.util.List;

public interface GenericDAO<T, ID> {
    void create(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findRange(int first, int pageSize);
    List<T> findAll();
    int count();
    EntityManager getEntityManager();  // para permitir .remove() desde resource
}
