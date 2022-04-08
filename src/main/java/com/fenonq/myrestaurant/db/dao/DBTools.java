package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.entity.enums.Locales;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTools {
    private static final Logger log = LogManager.getLogger(DBTools.class.getName());

    public static final String SELECT_FROM_DISH = "SELECT dish.*, dish_description.name, dish_description.description FROM dish INNER JOIN dish_description ON dish.id = dish_description.dish_id WHERE language_id = ?";
    public static final String SELECT_COUNT_DISH = "SELECT COUNT(*) FROM dish WHERE is_visible = 1";
    public static final String SELECT_DISH_BY_ID = "SELECT dish.*, dish_description.name, dish_description.description FROM dish INNER JOIN dish_description ON dish.id = dish_description.dish_id WHERE dish.id = ? AND language_id = ?";
    public static final String SELECT_DISH_DESCRIPTION_BY_ID = "SELECT * FROM dish_description WHERE dish_id = ?";
    public static final String SELECT_SUB_DISH_BY_ID = "SELECT * FROM dish WHERE id = ?";
    public static final String SELECT_SORTED_DISHES = "SELECT dish.*, dish_description.name, dish_description.description FROM dish INNER JOIN dish_description ON dish.id = dish_description.dish_id WHERE language_id = ? AND is_visible = 1 ORDER BY ";
    public static final String SELECT_DISHES_BY_CATEGORY = "SELECT dish.*, dish_description.name, dish_description.description FROM dish INNER JOIN dish_description ON dish.id = dish_description.dish_id WHERE language_id = ? AND category_id = ? AND is_visible = 1 ORDER BY ";
    public static final String SELECT_COUNT_DISH_BY_CATEGORY = "SELECT COUNT(*) FROM dish WHERE category_id = ? AND is_visible = 1";
    public static final String INSERT_INTO_DISH = "INSERT INTO dish VALUES (DEFAULT, ?, ?, ?, ?)";
    public static final String INSERT_INTO_DISH_DESCRIPTION = "INSERT INTO dish_description VALUES (?, ?, ?, ?)";
    public static final String DELETE_DISH = "DELETE FROM dish WHERE id = ?";
    public static final String UPDATE_DISH = "UPDATE dish SET price = ?, weight = ?, category_id = ?, is_visible = ? WHERE id = ?";
    public static final String UPDATE_DISH_DESCRIPTION = "UPDATE dish_description SET name = ?, description = ? WHERE dish_id = ? AND language_id = ?";
    public static final String UPDATE_DISH_STATUS = "UPDATE dish SET is_visible = ? WHERE id = ?";

    public static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
    public static final String INSERT_INTO_CATEGORY = "INSERT INTO category VALUES (DEFAULT, ?, ?)";
    public static final String UPDATE_CATEGORY = "UPDATE category SET name_en = ?, name_ua = ? WHERE id = ?";
    public static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";

    public static final String SELECT_FROM_USER = "SELECT * FROM user";
    public static final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
    public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    public static final String LOG_IN = "SELECT * FROM user WHERE login = ? AND password = ?";
    public static final String INSERT_INTO_USER = "INSERT INTO user VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    public static final String UPDATE_ROLE = "UPDATE user SET role_id = ? WHERE id = ?";
    public static final String DELETE_USER = "DELETE FROM user WHERE id = ?";
    public static final String UPDATE_PASSWORD = "UPDATE user SET password = ? WHERE id = ?";

    public static String SELECT_FROM_RECEIPT(Locales locale) {
        return "SELECT receipt.*, status.name_" + locale.name().toLowerCase() + " FROM receipt INNER JOIN status ON receipt.status_id = status.id";
    }
    public static String SELECT_RECEIPT_BY_USER_ID(Locales locale) {
        return "SELECT receipt.*, status.name_" + locale.name().toLowerCase() + " FROM receipt INNER JOIN status ON receipt.status_id = status.id WHERE user_id = ?";
    }
    public static String SELECT_RECEIPT_BY_MANAGER_ID(Locales locale) {
        return "SELECT receipt.*, status.name_" + locale.name().toLowerCase() + " FROM receipt INNER JOIN status ON receipt.status_id = status.id WHERE manager_id = ?";
    }
    public static String SELECT_RECEIPT_BY_STATUS_ID(Locales locale) {
        return "SELECT receipt.*, status.name_" + locale.name().toLowerCase() + " FROM receipt INNER JOIN status ON receipt.status_id = status.id WHERE status_id = ?";
    }
    public static final String UPDATE_STATUS_ID = "UPDATE receipt SET status_id = ?, manager_id = ? WHERE id = ?";

    public static final String SELECT_DISHES_BY_RECEIPT = "SELECT rhd.count, dish.*, dish_description.name, dish_description.description \n" +
            "FROM receipt_has_dish AS rhd INNER JOIN dish ON rhd.dish_id = dish.id INNER JOIN dish_description ON dish.id = dish_description.dish_id\n" +
            "WHERE receipt_id = ? AND language_id = ?";

    public static String SELECT_FROM_STATUS(Locales locale) {
        return "SELECT id, name_" + locale.name().toLowerCase() + " FROM status";
    }

    public static final String SELECT_USER_CART_BY_USER_ID = "SELECT user_cart.count, dish.*, dish_description.name, dish_description.description FROM user_cart INNER JOIN dish ON dish.id = user_cart.dish_id INNER JOIN dish_description ON user_cart.dish_id = dish_description.dish_id WHERE user_id = ? AND language_id = ?";
    public static final String INSERT_DISH_INTO_USER_CART = "INSERT INTO user_cart (user_id, dish_id, count) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE count = ?";
    public static final String DELETE_DISH_FROM_USER_CART = "DELETE FROM user_cart WHERE user_id = ? AND dish_id = ?";
    public static final String INSERT_INTO_RECEIPT = "INSERT INTO receipt (user_id, total_price) VALUES (?, ?)";
    public static final String INSERT_INTO_RECEIPT_HAS_DISH = "INSERT INTO receipt_has_dish VALUES (?, ?, ?)";
    public static final String DELETE_USER_CART = "DELETE FROM user_cart WHERE user_id = ?";

    public static String SELECT_FROM_CATEGORY(Locales locale) {
        return "SELECT id, name_" + locale.name().toLowerCase() + " FROM category";
    }

    public static void close(AutoCloseable stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                log.error("Error: " + e);
            }
        }
    }

    public static void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (Exception e) {
            log.error("Error: " + e);
        }
    }

    public static void autoCommit(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Error: " + e);
        }
    }
}
