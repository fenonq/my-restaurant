package com.fenonq.myrestaurant.db.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionSettings {
    private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    public Connection getConnection() throws SQLException {
        return connectionBuilder == null ?
                ConnectionPool.getInstance().getConnection()
                : connectionBuilder.getConnection();
    }
}
