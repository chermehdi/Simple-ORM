package io.mehdithe.github.core;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mehdithe
 */
public class AnnotationProcessorTest {

  Data data;
  AnnotationProcessor processor;

  @Before
  public void init() {
    data = new Data(1, "mehdi");
    processor = new AnnotationProcessor(data);
  }

  @Test
  public void getTableName() {
    assertNotNull(processor);
    assertThat(processor.getTableName(), is("DATA"));
  }

  @Test
  public void getFieldNames() {
    assertThat(processor.getFieldNames(), is(Arrays.asList("ID", "NAME")));
  }

  @Test
  public void getIdName() {
    assertThat(processor.getIdName(), is("id"));
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