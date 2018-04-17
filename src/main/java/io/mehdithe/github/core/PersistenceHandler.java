package io.mehdithe.github.core;

/**
 * @author mehdithe
 */
public interface PersistenceHandler {

  Object find(Long id);

  <T> T find(Long id, Class<T> clazz);

  Object save(Object object);

  Object update(Object object);

  Object delete(Object object);
}
