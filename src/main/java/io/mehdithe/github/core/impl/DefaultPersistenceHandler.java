package io.mehdithe.github.core.impl;

import io.mehdithe.github.core.AnnotationProcessor;
import io.mehdithe.github.core.DataSource;
import io.mehdithe.github.core.PersistenceHandler;
import java.sql.Connection;

/**
 * @author mehdithe
 */
public class DefaultPersistenceHandler implements PersistenceHandler {

  private DataSource dataSource;

  private AnnotationProcessor processor;

  private Connection engine;

  public DefaultPersistenceHandler(DataSource dataSource) {
    setDataSource(dataSource);
  }

  private void setDataSource(DataSource dataSource) {
    try {
      this.dataSource = dataSource;
      this.engine = dataSource.getConnection();
      processor = new AnnotationProcessor();
    } catch (Exception e) {
      System.out.println("Could Not Inject the datasource " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public Object find(Long id) {
    return null;
  }

  @Override
  public <T> T find(Long id, Class<T> clazz) {
    return null;
  }

  @Override
  public Object save(Object object) {
    return null;
  }

  @Override
  public Object update(Object object) {
    return null;
  }

  @Override
  public Object delete(Object object) {
    return null;
  }
}
