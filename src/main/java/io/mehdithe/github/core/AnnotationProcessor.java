package io.mehdithe.github.core;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import java.lang.reflect.Field;
import java.util.Date;
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

  public String getIdColumnName() {
    Field[] fields = clazz.getDeclaredFields();
    String idName = null;
    int idCount = 0;
    for (Field field : fields) {
      if (field.isAnnotationPresent(Id.class)) {
        if (field.isAnnotationPresent(Column.class)) {
          idName = field.getAnnotation(Column.class).name();
        } else {
          idName = field.getName();
        }
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
      Object ret = field.get(object);
      if (ret == null) {
        ret = getDefaultValue(ret);
      }
      return ret;
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

  public void fillObject(Map<String, Object> values) {
    if (object == null) {
      init();
    }
    Map<String, String> correspondenceMap = getCorrespondenceMap();
    try {
      for (String colName : values.keySet()) {
        System.out.println(colName + " " + correspondenceMap.get(colName));
        Field field = clazz.getDeclaredField(correspondenceMap.get(colName));
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(object, values.get(colName));
        field.setAccessible(accessible);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * returns a map where the keys are the names of the columns in the database the values are the
   * names of their corresponding fields in the model
   */
  public Map<String, String> getCorrespondenceMap() {
    Map<String, String> cor = new HashMap<>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Column.class)) {
        String fieldName = field.getName();
        String colName = field.getAnnotation(Column.class).name();
        cor.put(colName, fieldName);
      } else {
        String fieldName = field.getName();
        cor.put(fieldName, fieldName);
      }
    }
    return cor;
  }

  public Long getId() {
    if (object == null) {
      throw new RuntimeException("cannot get Id on null object");
    }
    String idName = getIdName();
    try {
      Field field = clazz.getDeclaredField(idName);
      Object value = getFieldValue(field);
      return (Long) value;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0L;
  }

  private void init() {
    try {
      object = clazz.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Object getDefaultValue(Object obj) {
    if (obj instanceof Long || obj instanceof Integer || obj instanceof Double) {
      return 0.0;
    }
    if (obj instanceof CharSequence) {
      return "";
    }
    if (obj instanceof Date) {
      return new Date();
    }
    return null;
  }

  private void setFieldValue(Field field, Object object, Object value) {
    try {
      boolean accessible = field.isAccessible();
      field.setAccessible(true);
      field.set(object, value);
      field.setAccessible(accessible);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return "AnnotationProcessor{" +
        "clazz=" + clazz +
        '}';
  }

  public void setPrimaryKey(Long id) throws NoSuchFieldException {
    String idName = getIdName();
    Field field = clazz.getDeclaredField(idName);
    setFieldValue(field, object, id);
  }
}
