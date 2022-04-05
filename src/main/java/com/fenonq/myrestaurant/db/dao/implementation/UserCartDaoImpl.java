package com.fenonq.myrestaurant.db.dao.implementation;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionSettings;
import com.fenonq.myrestaurant.db.dao.interfaces.UserCartDao;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.fenonq.myrestaurant.db.dao.DBTools.*;

public class UserCartDaoImpl extends ConnectionSettings implements UserCartDao {

    @Override
    public void addDishToCart(int userId, int dishId, int count) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_DISH_INTO_USER_CART)) {

            int k = 0;
            stmt.setInt(++k, userId);
            stmt.setInt(++k, dishId);
            stmt.setInt(++k, count);
            stmt.setInt(++k, count);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method addDishToCart userCart", e);
        }
    }

    @Override
    public Map<Dish, Integer> getUserCart(int userId, Locales locale) throws DBException {
        Map<Dish, Integer> cart = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USER_CART_BY_USER_ID)) {

            int k = 0;
            stmt.setInt(++k, userId);
            stmt.setInt(++k, locale.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dish dish = DishDaoImpl.dishMapper(rs);
                    int count = rs.getInt("count");
                    cart.put(dish, count);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method getUserCart", e);
        }
        return cart;
    }

    @Override
    public void removeDishFromCart(int userId, int dishId) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_DISH_FROM_USER_CART)) {

            int k = 0;
            stmt.setInt(++k, userId);
            stmt.setInt(++k, dishId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Error in method removeDishFromCart", e);
        }
    }

    @Override
    public void makeAnOrder(int userId, Map<Dish, Integer> cart) throws DBException {
        Connection con = null;
        try {
            con = getConnection();

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            int totalPrice = 0;
            for (Map.Entry<Dish, Integer> entry : cart.entrySet()) {
                totalPrice += entry.getKey().getPrice() * entry.getValue();
            }

            int receiptId = addReceipt(con, userId, totalPrice);
            for (Map.Entry<Dish, Integer> entry : cart.entrySet()) {
                addReceiptHasDish(con, receiptId, entry.getKey().getId(), entry.getValue());
            }
            con.commit();
        } catch (SQLException e) {
            rollback(con);
            throw new DBException("Error in method makeAnOrder", e);
        } finally {
            autoCommit(con);
            close(con);
        }
    }

    private void addReceiptHasDish(Connection c, int receiptId, int dishId, int count) throws SQLException {
        try (PreparedStatement stmt = c.prepareStatement(INSERT_INTO_RECEIPT_HAS_DISH)) {
            int k = 0;
            stmt.setInt(++k, receiptId);
            stmt.setInt(++k, dishId);
            stmt.setInt(++k, count);
            stmt.executeUpdate();
        }
    }

    private int addReceipt(Connection c, int userId, int totalPrice) throws SQLException {
        try (PreparedStatement stmt = c.prepareStatement(INSERT_INTO_RECEIPT, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            stmt.setInt(++k, userId);
            stmt.setInt(++k, totalPrice);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public void cleanUserCart(int userId) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_USER_CART)) {

            int k = 0;
            stmt.setInt(++k, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method cleanCart", e);
        }
    }
}
