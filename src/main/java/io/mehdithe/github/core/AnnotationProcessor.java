package io.mehdithe.github.core;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import java.lang.reflect.Field;
import java.util.List;
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
      String fieldName = null;
      if (field.isAnnotationPresent(Column.class)) {
        fieldName = field.getAnnotation(Column.class).name();
      } else {
        fieldName = field.getName();
      }
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

  public Class<?> getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return "AnnotationProcessor{" +
        "clazz=" + clazz +
        '}';
  }
}
