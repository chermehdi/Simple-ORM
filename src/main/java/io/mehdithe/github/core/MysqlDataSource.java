package io.mehdithe.github.core;

public class MysqlDataSource extends DataSource {

  public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

  public static final String MYSQL_BRIDGE = "jdbc:mysql:";

  MysqlDataSource() {}

  public MysqlDataSource(String username, String password, String source, String driver,
      String bridge, String host) {
    super(username, password, source, MYSQL_DRIVER, MYSQL_BRIDGE, host);
  }

  public MysqlDataSource(String username, String password, String source, String host) {
    super(username, password, source, MYSQL_DRIVER, MYSQL_BRIDGE, host);
  }

  public MysqlDataSource(String username, String source) {
    super(username, "", source, MYSQL_DRIVER, MYSQL_BRIDGE, "localhost");
  }

  public MysqlDataSource(String source) {
    super("root", "", source, MYSQL_DRIVER, MYSQL_BRIDGE, "localhost");
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
