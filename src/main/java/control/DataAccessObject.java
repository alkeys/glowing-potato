package control;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD.
 * @param <T> El tipo de la entidad.
 * @param <K> El tipo de la clave primaria (ID) de la entidad.
 */
public interface DataAccessObject<T, K> {
    void create(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(K id);
    List<T> findAll();
    List<T> findRange(int start, int size);
    int count();
}