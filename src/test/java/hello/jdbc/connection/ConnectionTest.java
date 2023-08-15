package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

  @Test
  void dataSourceTest() throws SQLException {
    Connection connection1 = DriverManager.getConnection(
        ConnectionConst.URL,
        ConnectionConst.USERNAME,
        ConnectionConst.PASSWORD);

    Connection connection2 = DriverManager.getConnection(
        ConnectionConst.URL,
        ConnectionConst.USERNAME,
        ConnectionConst.PASSWORD);

    log.info("connection = {}, class = {}", connection1 , connection1.getClass());
    log.info("connection = {}, class = {}", connection2 , connection2.getClass());

  }

  @Test
  void dataSourceConnectionPool() throws SQLException, InterruptedException {
    HikariDataSource hp = new HikariDataSource();
    hp.setJdbcUrl(ConnectionConst.URL);
    hp.setUsername(ConnectionConst.USERNAME);
    hp.setPassword(ConnectionConst.PASSWORD);
    hp.setMaximumPoolSize(10);
    hp.setPoolName("MyPool");

    Connection connection1 = hp.getConnection();
    Connection connection2 = hp.getConnection();

    log.info("connection = {}, class = {}", connection1 , connection1.getClass());
    log.info("connection = {}, class = {}", connection2 , connection2.getClass());

    Thread.sleep(1000);
  }

  @Test
  void driverManagerDataSourceTest() throws SQLException {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(
        ConnectionConst.URL,
        ConnectionConst.USERNAME,
        ConnectionConst.PASSWORD);

    Connection connection1 = dataSource.getConnection();
    Connection connection2 = dataSource.getConnection();

    log.info("connection = {}, class = {}", connection1 , connection1.getClass());
    log.info("connection = {}, class = {}", connection2 , connection2.getClass());
  }

}
