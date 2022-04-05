package com.fenonq.myrestaurant.db.dao.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool implements ConnectionBuilder {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class.getName());

    private static ConnectionPool instance;

    private final DataSource dataSource;

    private ConnectionPool() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/restaurant");
        } catch (NamingException e) {
            log.error("Error: " + e);
            throw new IllegalStateException("Cannot init DBManager", e);
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
