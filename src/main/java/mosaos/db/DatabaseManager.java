package mosaos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mosaos.conf.AppConfig;
import mosaos.exception.ApplicationException;


/**
 * Initialize DriverManager, etc.
 */
public class DatabaseManager {
    private static DatabaseManager instance = new DatabaseManager();

    private DatabaseManager() {
        try {
            Class.forName(AppConfig.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(AppConfig.getJdbcUrl(), AppConfig.getJdbcUser(), AppConfig.getJdbcPassword());
        return con;
    }
}