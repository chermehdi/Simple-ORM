package io.mehdithe.github.core.impl;

import io.mehdithe.github.core.AnnotationProcessor;
import io.mehdithe.github.core.DataSource;
import io.mehdithe.github.core.PersistenceHandler;
import io.mehdithe.github.core.Tuple;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mehdithe
 */
public class DefaultPersistenceHandler implements PersistenceHandler {

  private DataSource dataSource;

  private AnnotationProcessor processor;

  private Connection engine;

  String SELECT_TEMPLATE = "SELECT * FROM `%s` WHERE `%s`=%d";

  String INSERT_TEMPLATE = "INSERT INTO `%s` %s VALUES %s";

  String COUNT_TEMPLATE = "SELECT count(*) FROM `%s`";

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
    processor.setClazz(clazz);
    String tableName = processor.getTableName();
    String idCol = processor.getIdColumnName();
    String query = String.format(SELECT_TEMPLATE, tableName, idCol, id);
    List<String> fieldNames = processor.getFieldNames();
    try (Statement statement = engine.createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);
      Map<String, Object> values = new HashMap<>();
      while (resultSet.next()) {
        for (String fieldName : fieldNames) {
          values.put(fieldName, resultSet.getObject(fieldName));
        }
      }
      processor.fillObject(values);
      resultSet.close();
      return clazz.cast(processor.getObject());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * this will save the given object in it's appropriate table note that this will mutate the object
   * and set its primary key if the object given already exists in the database nothing will happen
   * and it will be returned as it is
   */
  @Override
  public Object save(Object object) {
    processor.setObject(object);
    Long id = processor.getId();
    // test if an entity already exists
    if (id != null && id > 0) {
      Object o = find(id, object.getClass());
      if (o != null) {
        return o;
      }
    }
    String tableName = processor.getTableName();
    try (Statement statement = engine.createStatement()) {
      processor.setPrimaryKey(getPrimaryKey(statement));
      Map<String, Object> valuesMap = processor.getFieldValues();
      Tuple names = new Tuple();
      Tuple values = new Tuple();
      for (String name : valuesMap.keySet()) {
        names.add(name);
        values.add(valuesMap.get(name));
      }
      String query = String
          .format(INSERT_TEMPLATE, tableName, names.toString(), values.toString(true));
      int returnValue = statement.executeUpdate(query);
      return processor.getObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return object;
  }

  private Long getPrimaryKey(Statement statement) {
    String tableName = processor.getTableName();
    try {
      ResultSet resultSet = statement.executeQuery(String.format(COUNT_TEMPLATE, tableName));
      resultSet.next();
      return (long) resultSet.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 1L;
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
