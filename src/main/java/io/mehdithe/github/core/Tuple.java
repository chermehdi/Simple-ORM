package io.mehdithe.github.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mehdithe
 */
public class Tuple {

  private List<Object> objects;

  public Tuple() {
    objects = new ArrayList<>();
  }

  public void add(Object obj) {
    objects.add(obj);
  }

  public String toString() {
    return toString(false);
  }

  public String toString(boolean values) {
    if (values) {
      List<String> elements = objects.stream()
          .map(this::escapeValues)
          .collect(Collectors.toList());
      return "(" + String.join(", ", elements) + ")";

    } else {
      List<String> elements = objects.stream()
          .map(this::escape)
          .collect(Collectors.toList());
      return "(" + String.join(", ", elements) + ")";
    }
  }

  private String escape(Object s) {
    if (s instanceof Integer || s instanceof Long || s instanceof Double) {
      return s + "";
    }
    return "`" + s.toString() + "`";
  }


  private String escapeValues(Object s) {
    if (s instanceof Integer || s instanceof Long || s instanceof Double) {
      return s + "";
    }
    return "'" + s.toString() + "'";
  }
}
