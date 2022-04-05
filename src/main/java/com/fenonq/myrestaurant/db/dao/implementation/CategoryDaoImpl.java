package com.fenonq.myrestaurant.db.dao.implementation;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionSettings;
import com.fenonq.myrestaurant.db.dao.interfaces.CategoryDao;
import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import static com.fenonq.myrestaurant.db.dao.DBTools.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDaoImpl extends ConnectionSettings implements CategoryDao {

    private Category categoryMapper(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setNameEn(rs.getString("name_en"));
        c.setNameUa(rs.getString("name_ua"));
        return c;
    }

    private int setAttributes(Category category, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, category.getNameEn());
        stmt.setString(++k, category.getNameUa());
        return k;
    }

    @Override
    public List<Category> findAll(Locales locale) throws DBException {
        List<Category> categories = new ArrayList<>();
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_CATEGORY(locale))) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name_" + locale.name().toLowerCase()));
                categories.add(c);
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAll categories", e);
        }
        return categories;
    }

    @Override
    public Category findById(int id, Locales locale) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_CATEGORY_BY_ID)) {

            int k = 0;
            stmt.setInt(++k, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return categoryMapper(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findById categories", e);
        }
    }

    @Override
    public void create(Category category) throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(INSERT_INTO_CATEGORY, Statement.RETURN_GENERATED_KEYS);

            setAttributes(category, stmt);

            int count = stmt.executeUpdate();
            if (count > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method create categories", e);
        } finally {
            close(stmt);
            close(con);
        }
    }

    @Override
    public void update(Category category) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_CATEGORY)) {

            int k = setAttributes(category, stmt);
            stmt.setInt(++k, category.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method update dish", e);
        }
    }

    @Override
    public void deleteById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_CATEGORY)) {

            int k = 0;
            stmt.setInt(++k, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method deleteById category", e);
        }
    }
}
