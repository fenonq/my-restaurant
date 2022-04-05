package com.fenonq.myrestaurant.db.dao.implementation;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionSettings;
import com.fenonq.myrestaurant.db.dao.interfaces.DishDao;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import static com.fenonq.myrestaurant.db.dao.DBTools.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DishDaoImpl extends ConnectionSettings implements DishDao {

    public static Dish dishMapper(ResultSet rs) throws SQLException {
        Dish d = new Dish();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setDescription(rs.getString("description"));
        d.setWeight(rs.getInt("weight"));
        d.setPrice(rs.getInt("price"));
        d.setCategoryId(rs.getInt("category_id"));
        return d;
    }

    private int setAttributesDish(SubDish dish, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setInt(++k, dish.getPrice());
        stmt.setInt(++k, dish.getWeight());
        stmt.setInt(++k, dish.getCategoryId());
        return k;
    }

    private void setAttributesDishDescription(DishDescription dishDescription, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setInt(++k, dishDescription.getDishId());
        stmt.setInt(++k, dishDescription.getLanguageId());
        stmt.setString(++k, dishDescription.getName());
        stmt.setString(++k, dishDescription.getDescription());
    }

    @Override
    public List<Dish> findAll(Locales locale) throws DBException {
        List<Dish> dishes = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_FROM_DISH)) {

            int k = 0;
            stmt.setInt(++k, locale.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dishes.add(dishMapper(rs));
                }
            }
        } catch (Exception e) {
            throw new DBException("Error in method findAll dishes", e);
        }
        return dishes;
    }

    @Override
    public Dish findById(int id, Locales locale) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_DISH_BY_ID)) {

            int k = 0;
            stmt.setInt(++k, id);
            stmt.setInt(++k, locale.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return dishMapper(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findById dishes", e);
        }
    }

    @Override
    public SubDish findSubDishById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_SUB_DISH_BY_ID)) {

            int k = 0;
            stmt.setInt(++k, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SubDish subDish = new SubDish();
                    subDish.setId(rs.getInt("id"));
                    subDish.setCategoryId(rs.getInt("category_id"));
                    subDish.setPrice(rs.getInt("price"));
                    subDish.setWeight(rs.getInt("weight"));
                    return subDish;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findById dishes", e);
        }
    }

    @Override
    public List<DishDescription> findLocalizationById(int id) throws DBException {
        List<DishDescription> dishDescriptions = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_DISH_DESCRIPTION_BY_ID)) {

            int k = 0;
            stmt.setInt(++k, id);

            try (ResultSet rs = stmt.executeQuery()) {
                DishDescription dishDescription;

                while (rs.next()) {
                    dishDescription = new DishDescription();
                    dishDescription.setLanguageId(rs.getInt("language_id"));
                    dishDescription.setDishId(rs.getInt("dish_id"));
                    dishDescription.setName(rs.getString("name"));
                    dishDescription.setDescription(rs.getString("description"));

                    dishDescriptions.add(dishDescription);
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findById dishes", e);
        }
        return dishDescriptions;
    }

    @Override
    public void createDish(SubDish dish, DishDescription... dishDescription) throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(INSERT_INTO_DISH, Statement.RETURN_GENERATED_KEYS);

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            setAttributesDish(dish, stmt);

            int count = stmt.executeUpdate();
            if (count > 0) {
                try (ResultSet rs1 = stmt.getGeneratedKeys()) {
                    if (rs1.next()) {
                        dish.setId(rs1.getInt(1));
                    }
                }
            }
            for (DishDescription description : dishDescription) {
                description.setDishId(dish.getId());
                addNewDishLocalization(description, con);
            }

            con.commit();

        } catch (SQLException | DBException e) {
            rollback(con);
            throw new DBException("Error in method createDish", e);
        } finally {
            autoCommit(con);
            close(stmt);
            close(con);
        }
    }

    private void addNewDishLocalization(DishDescription dishDescription, Connection con) throws DBException {
        PreparedStatement stmt = null;
        try {
            if (con == null) {
                con = getConnection();
            }
            stmt = con.prepareStatement(INSERT_INTO_DISH_DESCRIPTION);

            setAttributesDishDescription(dishDescription, stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Error in method addNewDishLocalization", e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void update(SubDish dish, DishDescription... dishDescription) throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(UPDATE_DISH);

            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            int k = setAttributesDish(dish, stmt);
            stmt.setInt(++k, dish.getId());

            stmt.executeUpdate();

            for (DishDescription description : dishDescription) {
                description.setDishId(dish.getId());
                updateDishLocalization(description, con);
            }
            con.commit();
        } catch (SQLException | DBException e) {
            rollback(con);
            throw new DBException("Error in method updateDish", e);
        } finally {
            autoCommit(con);
            close(stmt);
            close(con);
        }
    }

    private void updateDishLocalization(DishDescription dishDescription, Connection con) throws DBException {
        PreparedStatement stmt = null;
        try {
            if (con == null) {
                con = getConnection();
            }
            stmt = con.prepareStatement(UPDATE_DISH_DESCRIPTION);

            int k = 0;

            stmt.setString(++k, dishDescription.getName());
            stmt.setString(++k, dishDescription.getDescription());
            stmt.setInt(++k, dishDescription.getDishId());
            stmt.setInt(++k, dishDescription.getLanguageId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Error in method updateDishLocalization", e);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void deleteById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_DISH)) {

            int k = 0;
            stmt.setInt(++k, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method deleteById dish", e);
        }
    }

    @Override
    public List<Dish> findSortedByCategoryIdOnPage(Locales locale, String sortBy, int categoryId,
                                                   int dishesOnPage, int pageNum) throws DBException {
        List<Dish> dishes = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     SELECT_DISHES_BY_CATEGORY + sortBy + " LIMIT "
                             + dishesOnPage * pageNum + ", " + dishesOnPage)) {

            int k = 0;
            stmt.setInt(++k, locale.getId());
            stmt.setInt(++k, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dishes.add(dishMapper(rs));
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAllByCategoryIdOnPage dish", e);
        }
        return dishes;
    }

    @Override
    public List<Dish> findSortedDishesOnPage(Locales locale, String sortBy,
                                             int dishesOnPage, int pageNum) throws DBException {
        List<Dish> dishes = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     SELECT_SORTED_DISHES + sortBy + " LIMIT "
                             + dishesOnPage * pageNum + ", " + dishesOnPage)) {

            int k = 0;
            stmt.setInt(++k, locale.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dishes.add(dishMapper(rs));
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAllByCategoryIdOnPage dish", e);
        }
        return dishes;
    }

    @Override
    public int getDishesNumber() throws DBException {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_COUNT_DISH)) {

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new DBException("Error in method getDishesNumber dishes", e);
        }
    }

    @Override
    public int getDishesNumberInCategory(int categoryId) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_COUNT_DISH_BY_CATEGORY)) {

            int k = 0;
            stmt.setInt(++k, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAll dishes", e);
        }
    }

}
