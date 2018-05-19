package io.mehdithe.github.core;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author mehdithe
 */
public class DataSourceBuilderTest {

  @Test
  public void build() {
    DataSource source = new DataSourceBuilder("com.mysql.jdbc.Driver", "jdbc:mysql:")
        .source("Library")
        .host("localhost")
        .username("root")
        .password("root")
        .port(3306)
        .build();
    assertNotNull(source);
  }

}