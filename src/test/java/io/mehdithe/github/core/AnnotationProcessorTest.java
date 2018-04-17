package io.mehdithe.github.core;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mehdithe
 */
public class AnnotationProcessorTest {

  Data data;
  NoAnnotationData noAnnotationData;
  AnnotationProcessor processor;

  @Before
  public void init() {
    data = new Data(1, "mehdi");
    noAnnotationData = new NoAnnotationData(1, "mehdi");
    processor = new AnnotationProcessor(data);
  }

  @Test
  public void getTableName() {
    assertNotNull(processor);
    assertThat(processor.getTableName(), is("DATA"));
    processor.setObject(noAnnotationData);
    assertThat(processor.getTableName(), is("NoAnnotationData"));
  }

  @Test
  public void getFieldNames() {
    assertThat(processor.getFieldNames(), is(Arrays.asList("ID", "NAME")));
    processor.setObject(noAnnotationData);
    assertThat(processor.getFieldNames(), is(Arrays.asList("id", "name")));
  }

  @Test
  public void getIdName() {
    assertThat(processor.getIdName(), is("id"));
  }

  @Test
  public void getFieldValues() {
    Map<String, Object> expectedValues = new HashMap<>();
    expectedValues.put("ID", 1);
    expectedValues.put("NAME", "mehdi");
    assertThat(expectedValues, is(processor.getFieldValues()));
  }

}

@Table(name = "DATA")
class Data {

  @Id
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME")
  private String name;

  public Data(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class NoAnnotationData {

  @Id
  Integer id;

  String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public NoAnnotationData(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}