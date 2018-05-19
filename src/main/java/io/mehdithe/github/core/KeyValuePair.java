package io.mehdithe.github.core;

import java.util.Map;

/**
 * @author mehdithe
 */
public class KeyValuePair {

  private Map<String, Object> values;

  public KeyValuePair(Map<String, Object> values) {
    this.values = values;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    int count = 0;
    int size = values.keySet().size();
    for (String key : values.keySet()) {
      sb.append("`")
          .append(key)
          .append("`")
          .append("=")
          .append(escapeValues(values.get(key)));
      if(++count < size) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  private String escapeValues(Object s) {
    if (s instanceof Integer || s instanceof Long || s instanceof Double) {
      return s + "";
    }
    return "'" + s.toString() + "'";
  }
}
