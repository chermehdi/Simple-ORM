package io.mehdithe.github.core.models;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;

@Table(name = "authors")
public
class Author {

  @Id
  @Column(name = "Au_ID")
  private long id;

  @Column(name = "Author")
  private String name;

  @Column(name = "Year_Born")
  private int yearBorn;

  public Author() {

  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getYearBorn() {
    return yearBorn;
  }

  public void setYearBorn(int yearBorn) {
    this.yearBorn = yearBorn;
  }

  @Override
  public String toString() {
    return "Author{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", yearBorn=" + yearBorn +
        '}';
  }
}