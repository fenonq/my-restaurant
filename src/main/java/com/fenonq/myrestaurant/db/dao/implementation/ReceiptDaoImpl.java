package com.fenonq.myrestaurant.db.dao.implementation;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionSettings;
import com.fenonq.myrestaurant.db.dao.interfaces.ReceiptDao;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.Receipt;
import com.fenonq.myrestaurant.db.entity.Status;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fenonq.myrestaurant.db.dao.DBTools.*;

public class ReceiptDaoImpl extends ConnectionSettings implements ReceiptDao {

    private Receipt receiptMapper(ResultSet rs) throws SQLException {
        Receipt r = new Receipt();
        r.setId(rs.getInt("id"));
        r.setUserId(rs.getInt("user_id"));
        r.setManagerId(rs.getInt("manager_id"));
        r.setTotalPrice(rs.getInt("total_price"));
        r.setCreateDate(rs.getTimestamp("create_date"));
        return r;
    }

    @Override
    public List<Receipt> findAll(Locales locale) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_RECEIPT(locale))) {

            while (rs.next()) {
                Receipt receipt = receiptMapper(rs);
                receipt.setStatus(new Status(rs.getInt("status_id"), rs.getString("name_" + locale.name().toLowerCase())));
                receipt.setDishes(findDishesByReceipt(receipt.getId(), locale));
                receipts.add(receipt);
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAll receipts", e);
        }
        return receipts;
    }

    @Override
    public Receipt findById(int id, Locales locale) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_FROM_RECEIPT_BY_ID(locale))) {

            int k = 0;
            stmt.setInt(++k, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Receipt receipt = receiptMapper(rs);
                    receipt.setStatus(new Status(rs.getInt("status_id"), rs.getString("name_" + locale.name().toLowerCase())));
                    receipt.setDishes(findDishesByReceipt(receipt.getId(), locale));
                    return receipt;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Error in method findById receipts", e);
        }
    }

    @Override
    public void deleteById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_RECEIPT)) {

            int k = 0;
            stmt.setInt(++k, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method deleteById receipt", e);
        }
    }

    @Override
    public List<Receipt> findUserReceipts(int userId, Locales locale) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_RECEIPT_BY_USER_ID(locale))) {

            int k = 0;
            stmt.setInt(++k, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Receipt receipt = receiptMapper(rs);
                    receipt.setStatus(new Status(rs.getInt("status_id"), rs.getString("name_" + locale.name().toLowerCase())));
                    receipt.setDishes(findDishesByReceipt(receipt.getId(), locale));
                    receipts.add(receipt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Error in method findUserReceipts receipts", e);
        }
        return receipts;
    }

    private Map<Dish, Integer> findDishesByReceipt(int receiptId, Locales locale) throws DBException {
        Map<Dish, Integer> dishes = new HashMap<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_DISHES_BY_RECEIPT)) {

            int k = 0;
            stmt.setInt(++k, receiptId);
            stmt.setInt(++k, locale.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dish dish = DishDaoImpl.dishMapper(rs);
                    int count = rs.getInt("count");
                    dishes.put(dish, count);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findDishesByReceipt receipts", e);
        }
        return dishes;
    }

    @Override
    public List<Receipt> findAllReceiptsAcceptedBy(int managerId, Locales locale) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_RECEIPT_BY_MANAGER_ID(locale))) {

            int k = 0;
            stmt.setInt(++k, managerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Receipt receipt = receiptMapper(rs);
                    receipt.setStatus(new Status(rs.getInt("status_id"), rs.getString("name_" + locale.name().toLowerCase())));
                    receipt.setDishes(findDishesByReceipt(receipt.getId(), locale));
                    receipts.add(receipt);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAllReceiptsAcceptedBy receipts", e);
        }
        return receipts;
    }

    @Override
    public List<Receipt> findByStatusId(int statusId, Locales locale) throws DBException {
        List<Receipt> receipts = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_RECEIPT_BY_STATUS_ID(locale))) {

            int k = 0;
            stmt.setInt(++k, statusId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Receipt receipt = receiptMapper(rs);
                    receipt.setStatus(new Status(rs.getInt("status_id"), rs.getString("name_" + locale.name().toLowerCase())));
                    receipt.setDishes(findDishesByReceipt(receipt.getId(), locale));
                    receipts.add(receipt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Error in method findByStatusId receipts", e);
        }
        return receipts;
    }

    @Override
    public void changeStatus(int receiptId, int statusId, int managerId) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_STATUS_ID)) {

            int k = 0;
            stmt.setInt(++k, statusId);
            stmt.setInt(++k, managerId);
            stmt.setInt(++k, receiptId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Error in method changeStatus receipts", e);
        }
    }

    @Override
    public List<Status> getAllStatus(Locales locale) throws DBException {
        List<Status> statuses = new ArrayList<>();
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_STATUS(locale))) {

            while (rs.next()) {
                Status s = new Status();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name_" + locale.name().toLowerCase()));
                statuses.add(s);
            }
        } catch (SQLException e) {
            throw new DBException("Error in method getAllStatus statuses", e);
        }
        return statuses;
    }
}
