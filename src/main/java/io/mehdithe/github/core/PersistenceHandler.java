package io.mehdithe.github.core;

import java.util.Collection;
import java.util.List;

/**
 * @author mehdithe
 */
public interface PersistenceHandler {

  <T> T find(Long id, Class<T> clazz);

  Object save(Object object);

  Object update(Object object);

  Object delete(Object object);

  Collection<Object> saveAll(Iterable<Object> objects);

}
