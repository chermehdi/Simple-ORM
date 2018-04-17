package io.mehdithe.github.core;

/**
 * @author mehdithe
 */
public class DefaultDataSource extends DataSource {

  DefaultDataSource() {
  }

  @Override
  public String getURL() {
    return getBridge() + "//" + getHost() + "/" + getSource();
  }

  @Override
  public String startDelimiter() {
    return "`";
  }

  @Override
  public String endDelimiter() {
    return "`";
  }
}
