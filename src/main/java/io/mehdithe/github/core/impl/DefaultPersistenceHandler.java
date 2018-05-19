package io.mehdithe.github.core.impl;

import io.mehdithe.github.core.AnnotationProcessor;
import io.mehdithe.github.core.DataSource;
import io.mehdithe.github.core.KeyValuePair;
import io.mehdithe.github.core.PersistenceHandler;
import io.mehdithe.github.core.Tuple;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author mehdithe
 */
public class DefaultPersistenceHandler implements PersistenceHandler {

  private DataSource dataSource;


  private Connection engine;

  final String SELECT_TEMPLATE = "SELECT * FROM `%s` WHERE `%s`=%d";

  final String INSERT_TEMPLATE = "INSERT INTO `%s` %s VALUES %s";

  final String COUNT_TEMPLATE = "SELECT count(*) FROM `%s`";

  final String UPDATE_TEMPLATE = "UPDATE `%s` SET %s WHERE `%s`=%d";

  final String DELETE_TEMPLATE = "DELETE FROM `%s` WHERE `%s`=%d";

  public DefaultPersistenceHandler(DataSource dataSource) {
    setDataSource(dataSource);
  }

  private void setDataSource(DataSource dataSource) {
    try {
      this.dataSource = dataSource;
      this.engine = dataSource.getConnection();
    } catch (Exception e) {
      System.out.println("Could Not Inject the datasource " + e.getMessage());
      e.printStackTrace();
    }
  }

  @Override
  public <T> T find(Long id, Class<T> clazz) {
    AnnotationProcessor processor = AnnotationProcessor.from(clazz);
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
      if (values.isEmpty()) {
        return null;
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
    AnnotationProcessor processor = AnnotationProcessor.from(object.getClass());
    processor.setObject(object);
    Long id = processor.getIdValue();
    // test if an entity already exists
    if (id != null && id > 0) {
      Object o = find(id, object.getClass());
      if (o != null) {
        return o;
      }
    }
    String tableName = processor.getTableName();
    try (Statement statement = engine.createStatement()) {
      processor.setPrimaryKey(getPrimaryKey(statement, processor));
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

  private Long getPrimaryKey(Statement statement, AnnotationProcessor processor) {
    String tableName = processor.getTableName();
    try {
      ResultSet resultSet = statement.executeQuery(String.format(COUNT_TEMPLATE, tableName));
      resultSet.next();
      return (long) resultSet.getInt(1) + 1;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 1L;
  }

  @Override
  public Object update(Object object) {
    AnnotationProcessor processor = AnnotationProcessor.from(object.getClass());
    processor.setObject(object);
    Long id = processor.getIdValue();
    Object found = find(id, object.getClass());
    if (found == null) {
      throw new RuntimeException("Cannot update object, no reference exists for id " + id);
    }
    Long foundId = processor.getIdValue(found);
    if (!foundId.equals(id)) {
      // this test is for insurance should be removed
      throw new RuntimeException();
    }
    Map<String, Object> values = processor.getFieldValues();
    KeyValuePair pairs = new KeyValuePair(values);
    String tableName = processor.getTableName();
    String idColName = processor.getIdColumnName();
    String query = String.format(UPDATE_TEMPLATE, tableName, pairs.toString(), idColName, id);
    try (Statement statement = engine.createStatement()) {
      int ret = statement.executeUpdate(query);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return object;
  }

  @Override
  public Object delete(Object object) {
    AnnotationProcessor processor = AnnotationProcessor.from(object.getClass());
    processor.setObject(object);
    String idName = processor.getIdColumnName();
    Long idValue = processor.getIdValue();
    Object found = find(idValue, object.getClass());
    if(found == null) {
      throw new RuntimeException("no row found for the given object");
    }
    String tableName = processor.getTableName();
    try (Statement statement = engine.createStatement()) {
      String query = String.format(DELETE_TEMPLATE, tableName, idName, idValue);
      statement.executeUpdate(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return object;
  }

  @Override
  public Collection<Object> saveAll(Iterable<Object> objects) {
    List<Object> savedObjects = new Vector<>();
    for (Object object : objects) {
      savedObjects.add(save(object));
    }
    return savedObjects;
  }
}
