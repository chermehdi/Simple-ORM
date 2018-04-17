package io.mehdithe.github.core;

import java.util.Objects;

/**
 * @author mehdithe
 */
public class DataSourceBuilder {

  private DataSource dataSource;

  public DataSourceBuilder(String driver, String bridge) {
    dataSource = new DefaultDataSource();
    dataSource.driver = driver;
    dataSource.bridge = bridge;
  }

  public DataSourceBuilder username(String username) {
    dataSource.username = username;
    return this;
  }

  public DataSourceBuilder password(String password) {
    dataSource.password = password;
    return this;
  }

  public DataSourceBuilder host(String host) {
    dataSource.host = host;
    return this;
  }


  public DataSourceBuilder source(String source) {
    dataSource.source = source;
    return this;
  }

  public DataSourceBuilder port(int port) {
    dataSource.port = port;
    return this;
  }

  public DataSource build() {
    validateDataSource();
    setDefaultValues();
    return dataSource;
  }

  private void setDefaultValues() {
    if (dataSource.port == 0) {
      dataSource.port = 3306; // suppose it's mysql
    }
    if (dataSource.host == null) {
      dataSource.host = "localhost";
    }
  }

  private void validateDataSource() {
    Objects.requireNonNull(dataSource.driver);
    Objects.requireNonNull(dataSource.bridge);
    Objects.requireNonNull(dataSource.source);
  }

  @Override
  public String toString() {
    return "DataSourceBuilder{" +
        "dataSource=" + dataSource +
        '}';
  }
}
