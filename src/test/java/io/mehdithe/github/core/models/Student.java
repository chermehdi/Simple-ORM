package io.mehdithe.github.core.models;

import io.mehdithe.github.annotations.Column;
import io.mehdithe.github.annotations.Id;
import io.mehdithe.github.annotations.Table;

/**
 * @author mehdithe
 */
@Table(name = "students")
public class Student {

  @Id
  private Long id;

  private String name;

  private int age;

  public Student() {

  }

  public Student(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
