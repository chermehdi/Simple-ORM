package io.mehdithe.github.core.impl;

import static org.junit.Assert.*;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;
import io.mehdithe.github.core.DataSource;
import io.mehdithe.github.core.DataSourceBuilder;
import io.mehdithe.github.core.PersistenceHandler;
import io.mehdithe.github.core.models.Author;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mehdithe
 */
public class DefaultPersistenceHandlerTest {

  private DataSource dataSource;

  private PersistenceHandler handler;

  @Before
  public void init() {
    dataSource = new DataSourceBuilder("com.mysql.jdbc.Driver", "jdbc:mysql:")
        .source("Biblio")
        .host("localhost")
        .username("root")
        .password("root")
        .port(3306)
        .build();
    handler = new DefaultPersistenceHandler(dataSource);
  }

  @Test
  public void find() {
    Author author = handler.find(10L, Author.class);
    assertNotNull(author);
    assertEquals(author.getId(), 10);
  }

  @Test
  public void save() {
    Author p = new Author();
    p.setName("mehdi cheracher");
    p.setYearBorn(1231);
    handler.save(p);
    assertNotNull(p);
    assertTrue(p.getId() > 0);
  }

  @Test
  public void update() {
  }

  @Test
  public void delete() {
  }
}