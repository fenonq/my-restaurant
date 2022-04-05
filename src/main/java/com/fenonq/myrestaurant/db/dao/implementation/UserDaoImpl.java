package com.fenonq.myrestaurant.db.dao.implementation;

import com.fenonq.myrestaurant.db.dao.connection.ConnectionSettings;
import com.fenonq.myrestaurant.db.dao.interfaces.UserDao;
import com.fenonq.myrestaurant.db.entity.User;
import com.fenonq.myrestaurant.exception.DBException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.fenonq.myrestaurant.db.dao.DBTools.*;

public class UserDaoImpl extends ConnectionSettings implements UserDao {

    private String hashPass(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashPassword = new StringBuilder();
            for (byte b : bytes) {
                hashPassword.append(b);
            }
            return hashPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }

    private User userMapper(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setSurname(rs.getString("surname"));
        u.setLogin(rs.getString("login"));
        u.setPassword(rs.getString("password"));
        u.setRoleId(rs.getInt("role_id"));
        return u;
    }

    private void setAttributes(User user, PreparedStatement stmt) throws SQLException {
        int k = 0;
        stmt.setString(++k, user.getName());
        stmt.setString(++k, user.getSurname());
        stmt.setString(++k, user.getLogin());
        stmt.setString(++k, user.getPassword());
        stmt.setInt(++k, user.getRoleId());
    }

    @Override
    public List<User> findAll() throws DBException {
        List<User> users = new ArrayList<>();

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_USER)) {

            while (rs.next()) {
                users.add(userMapper(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findAll users", e);
        }
        return users;
    }

    @Override
    public User findById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USER_BY_ID)) {

            int k = 0;
            stmt.setInt(++k, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return userMapper(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findById users", e);
        }
    }

    @Override
    public User signUp(User user) throws DBException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = getConnection();
            stmt = con.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS);

            int roleIdUser = 2;
            user.setPassword(hashPass(user.getPassword()));
            user.setRoleId(roleIdUser);
            setAttributes(user, stmt);

            int count = stmt.executeUpdate();
            if (count > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
            }
            return findUserByLogin(user.getLogin());
        } catch (SQLException e) {
            throw new DBException("Error in method signUp users", e);
        } finally {
            close(stmt);
            close(con);
        }
    }

    @Override
    public User logIn(String login, String password) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(LOG_IN)) {

            int k = 0;
            stmt.setString(++k, login);
            stmt.setString(++k, hashPass(password));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return userMapper(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method logIn user", e);
        }
    }

    @Override
    public void changePassword(int userId, String newPassword) throws DBException {
        String hashPassword = hashPass(newPassword);
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_PASSWORD)) {
            int k = 0;
            stmt.setString(++k, hashPassword);
            stmt.setLong(++k, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method changePassword", e);
        }
    }

    @Override
    public void deleteById(int id) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_USER)) {

            int k = 0;
            stmt.setInt(++k, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method deleteById user", e);
        }
    }

    @Override
    public User findUserByLogin(String login) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_USER_BY_LOGIN)) {

            int k = 0;
            stmt.setString(++k, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return userMapper(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DBException("Error in method findByLogin users", e);
        }
    }

    @Override
    public void changeRole(int userId, int roleId) throws DBException {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_ROLE)) {

            int k = 0;
            stmt.setInt(++k, roleId);
            stmt.setInt(++k, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DBException("Error in method changeRole user", e);
        }
    }
}
