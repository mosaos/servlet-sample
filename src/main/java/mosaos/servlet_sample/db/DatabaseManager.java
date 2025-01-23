package mosaos.servlet_sample.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import mosaos.servlet_sample.conf.AppConfig;

/**
 * Initialize DriverManager, etc.
 */
public class DatabaseManager {
    private static DatabaseManager instance = new DatabaseManager();
    private HikariDataSource ds;

    private DatabaseManager() {
        HikariConfig conf = new HikariConfig();
        conf.setDriverClassName(AppConfig.getJdbcDriver());
        conf.setJdbcUrl(AppConfig.getJdbcUrl());
        conf.setUsername(AppConfig.getJdbcUser());
        conf.setPassword(AppConfig.getJdbcPassword());
        ds = new HikariDataSource(conf);
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = ds.getConnection();
        return con;
    }
}