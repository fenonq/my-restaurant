package com.fenonq.myrestaurant.db.dao.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDBTools {
    public static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE category(" +
                    "    id      INT AUTO_INCREMENT PRIMARY KEY," +
                    "    name_en VARCHAR(128) NOT NULL UNIQUE," +
                    "    name_ua VARCHAR(128) NOT NULL UNIQUE" +
                    ")";

    public static final String CREATE_TABLE_DISH =
            "CREATE TABLE IF NOT EXISTS dish(" +
                    "    id          INT AUTO_INCREMENT PRIMARY KEY," +
                    "    price       INT NOT NULL," +
                    "    weight      INT NOT NULL," +
                    "    category_id INT NOT NULL," +
                    "    is_visible  BOOLEAN DEFAULT 1" +
                    ")";

    public static final String CREATE_TABLE_DISH_DESCRIPTION =
            "CREATE TABLE IF NOT EXISTS dish_description(" +
                    "    dish_id     INT          NOT NULL," +
                    "    language_id INT          NOT NULL," +
                    "    name        VARCHAR(128) NOT NULL UNIQUE," +
                    "    description VARCHAR(512) NOT NULL UNIQUE" +
                    ")";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS user(" +
                    "    id       INT AUTO_INCREMENT PRIMARY KEY," +
                    "    name     VARCHAR(128) NOT NULL," +
                    "    surname  VARCHAR(128) NOT NULL," +
                    "    login    VARCHAR(64)  NOT NULL UNIQUE," +
                    "    password VARCHAR(256) NOT NULL," +
                    "    role_id  INT          NOT NULL" +
                    ")";

    public static final String CREATE_TABLE_RECEIPT =
            "CREATE TABLE IF NOT EXISTS receipt(" +
                    "    id          INT AUTO_INCREMENT PRIMARY KEY," +
                    "    user_id     INT       NOT NULL," +
                    "    manager_id  INT," +
                    "    status_id   INT       NOT NULL DEFAULT 1," +
                    "    total_price INT       NOT NULL DEFAULT 0," +
                    "    create_date TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public static final String CREATE_TABLE_STATUS =
            "CREATE TABLE IF NOT EXISTS status(" +
                    "    id      INT PRIMARY KEY," +
                    "    name_en VARCHAR(32) NOT NULL UNIQUE," +
                    "    name_ua VARCHAR(32) NOT NULL UNIQUE" +
                    ")";

    public static final String CREATE_TABLE_RECEIPT_HAS_DISH =
            "CREATE TABLE IF NOT EXISTS receipt_has_dish(" +
                    "    receipt_id INT NOT NULL," +
                    "    dish_id    INT NOT NULL," +
                    "    count      INT NULL DEFAULT 1" +
                    ")";

    public static final String CREATE_TABLE_USER_CART =
            "CREATE TABLE IF NOT EXISTS user_cart(" +
                    "    user_id INT NOT NULL," +
                    "    dish_id INT NOT NULL," +
                    "    count   INT NOT NULL DEFAULT 1" +
                    ");";


    public static String INSERT_INTO_RECEIPT(int userId, int managerId, int statusId) {
        return "INSERT INTO receipt VALUES (DEFAULT, "
                + userId + ", " + managerId + ", " + statusId + ", 123, DEFAULT)";
    }

    public static String INSERT_INTO_STATUS(int statusId) {
        return "INSERT INTO status VALUES (" + statusId + ", 'Test" + statusId + "', 'Тест" + statusId + "');";
    }

    public static final String DROP_TABLE_CATEGORY = "DROP TABLE category";
    public static final String DROP_TABLE_DISH = "DROP TABLE dish";
    public static final String DROP_TABLE_DISH_DESCRIPTION = "DROP TABLE dish_description";
    public static final String DROP_TABLE_USER = "DROP TABLE user";
    public static final String DROP_TABLE_RECEIPT = "DROP TABLE receipt";
    public static final String DROP_TABLE_STATUS = "DROP TABLE status";
    public static final String DROP_TABLE_RECEIPT_HAS_DISH = "DROP TABLE receipt_has_dish";
    public static final String DROP_TABLE_USER_CART = "DROP TABLE user_cart";

    public static final String CREATE_DATABASE = "CREATE DATABASE testdb";
    public static final String USE_DATABASE = "USE testdb";
    public static final String DROP_DATABASE = "DROP DATABASE testdb";


    public static String DB_URL;
    public static String USER;
    public static String PASSWORD;
    public static String FULL_URL;

    static {
        try {
            InputStream input =
                    new FileInputStream("src/test/java/com/fenonq/myrestaurant/db/dao/util/testDB.properties");
            Properties properties = new Properties();
            properties.load(input);

            DB_URL = properties.getProperty("db.url");
            USER = properties.getProperty("user");
            PASSWORD = properties.getProperty("password");
            FULL_URL = properties.getProperty("full.url");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
