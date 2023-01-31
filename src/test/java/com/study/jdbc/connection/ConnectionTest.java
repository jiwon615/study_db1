package com.study.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.study.jdbc.connection.ConnectionConstant.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection conn1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection conn2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("conn1={}, class={}", conn1, conn1.getClass());
        log.info("conn2={}, class={}", conn2, conn2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
        log.info("conn1={}, class={}", conn1, conn1.getClass());
        log.info("conn2={}, class={}", conn2, conn2.getClass());
    }
}
