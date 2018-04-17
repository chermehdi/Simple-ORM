package io.mehdithe.github.core;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * @author mehdithe
 */
public class AnnotationProcessor {

  private Class<?> clazz;
  private Object object;

  public AnnotationProcessor(Object object) {
    this.clazz = object.getClass();
    this.object = object;
  }

  public AnnotationProcessor() {
  }

  public String getTableName() {
    String tableName = clazz.getSimpleName();
    if (clazz.isAnnotationPresent(Table.class)) {
      tableName = clazz.getAnnotation(Table.class).name();
    }
    return tableName;
  }

  public List<String> getFieldNames() {
    Field[] fields = clazz.getDeclaredFields();
    List<String> fieldNames = new Vector<>();
    for (Field field : fields) {
      String fieldName = getFieldName(field);
      fieldNames.add(fieldName);
    }
    return fieldNames;
  }

  public String getIdName() {
    Field[] fields = clazz.getDeclaredFields();
    String idName = null;
    int idCount = 0;
    for (Field field : fields) {
      if (field.isAnnotationPresent(Id.class)) {
        idName = field.getName();
        ++idCount;
      }
    }
    if (idCount == 1) {
      return idName;
    } else {
      throw new RuntimeException("the number of fields annotated by Id is different than one");
    }
  }

  public Map<String, Object> getFieldValues() {
    Map<String, Object> values = new HashMap<>();
    for (Field field : clazz.getDeclaredFields()) {
      String fieldName = getFieldName(field);
      Object value = getFieldValue(field);
      values.put(fieldName, value);
    }
    return values;
  }

  private String getFieldName(Field field) {
    String fieldName = field.getName();
    if (field.isAnnotationPresent(Column.class)) {
      fieldName = field.getAnnotation(Column.class).name();
    }
    Objects.requireNonNull(fieldName);
    return fieldName;
  }

  private Object getFieldValue(Field field) {
    boolean visibility = field.isAccessible();
    try {
      field.setAccessible(true);
      return field.get(object);
    } catch (Exception e) {
      throw new RuntimeException("could not get field value " + e.getMessage());
    } finally {
      field.setAccessible(visibility);
    }
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
    setClazz(object.getClass());
  }

  @Override
  public String toString() {
    return "AnnotationProcessor{" +
        "clazz=" + clazz +
        '}';
  }
}
