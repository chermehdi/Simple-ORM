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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author mehdithe
 */
public class DefaultPersistenceHandlerTest {

  private DataSource dataSource;

  private PersistenceHandler handler;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void init() {
    dataSource = new DataSourceBuilder("com.mysql.jdbc.Driver", "jdbc:mysql:")
        .source("simple-orm-test")
        .host("localhost")
        .username("root")
        .password("root")
        .port(3306)
        .build();
    handler = new DefaultPersistenceHandler(dataSource);
  }

  @Test
  public void find() {
    Author author = handler.find(1L, Author.class);
    assertNotNull(author);
    assertEquals(author.getId(), 1);
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
    Author author = handler.find(1L, Author.class);
    author.setName(author.getName() + "-new");
    System.out.println("author is " + author);
    handler.update(author);
    assertNotNull(author);
    assertTrue(author.getName().endsWith("-new"));
  }

  @Test
  public void testUpdateThrowsRuntimeException() {
    expectedException.expect(RuntimeException.class);
    Author author = new Author();
    author.setYearBorn(123);
    handler.update(author);
  }

  @Test
  public void delete() {
    Author author = handler.find(1L, Author.class);
    assertNotNull(author);
    handler.delete(author);
    expectedException.expect(RuntimeException.class);
    handler.delete(author);
  }
}