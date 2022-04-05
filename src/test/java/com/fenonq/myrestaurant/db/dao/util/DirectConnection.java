package com.fenonq.myrestaurant.db.dao.util;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DirectConnection implements ConnectionBuilder {

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(TestDBTools.FULL_URL);
    }
}
