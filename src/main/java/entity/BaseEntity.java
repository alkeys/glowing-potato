package entity;

/**
 * Interfaz para entidades que tienen una clave primaria.
 * @param <K> El tipo de la clave primaria.
 */
public interface BaseEntity<K> {
    K getId();
    void setId(K id);
}