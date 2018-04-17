package io.mehdithe.github.core;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author mehdithe
 */
abstract public class DataSource {

  protected String username;
  protected String password;
  protected String source;
  protected String driver;
  protected String bridge;
  protected String host;

  protected int port;

  public DataSource() {
  }

  public DataSource(String username, String password, String source, String driver, String bridge,
      String host) {
    super();
    this.username = username;
    this.password = password;
    this.source = source;
    this.driver = driver;
    this.bridge = bridge;
    this.host = host;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }


  public void setPassword(String password) {
    this.password = password;
  }


  public String getSource() {
    return source;
  }


  public void setSource(String source) {
    this.source = source;
  }


  public String getDriver() {
    return driver;
  }


  public void setDriver(String driver) {
    this.driver = driver;
  }


  public String getBridge() {
    return bridge;
  }


  public void setBridge(String bridge) {
    this.bridge = bridge;
  }


  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Connection getConnection() {
    try {
      Class.forName(driver);
      return DriverManager.getConnection(getURL(), username, password);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  abstract public String getURL();

  abstract public String startDelimiter();

  abstract public String endDelimiter();

  @Override
  public String toString() {
    return "DataSource{" +
        "username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", source='" + source + '\'' +
        ", driver='" + driver + '\'' +
        ", bridge='" + bridge + '\'' +
        ", host='" + host + '\'' +
        ", port=" + port +
        '}';
  }
}
