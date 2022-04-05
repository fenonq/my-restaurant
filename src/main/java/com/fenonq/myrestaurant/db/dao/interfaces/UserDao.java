package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.User;
import com.fenonq.myrestaurant.exception.DBException;

import java.util.List;

public interface UserDao {
    /**
     * Finds all users stored in the table
     *
     * @return List of User objects
     */
    List<User> findAll() throws DBException;

    /**
     * Finds user by id
     *
     * @param id unique entity identifier
     * @return User object
     */
    User findById(int id) throws DBException;

    /**
     * Creates new user in the table
     *
     * @param user object to create
     */
    User signUp(User user) throws DBException;

    /**
     * Return null if there isn't user with this login and password
     * Otherwise, return user object
     *
     * @param login    login of user
     * @param password original(not hashed) password
     * @return user or null if there isn't user with this login and password
     */
    User logIn(String login, String password) throws DBException;

    /**
     * Changes user password in db
     *
     * @param userId      user identifier
     * @param newPassword new password to change
     */
    void changePassword(int userId, String newPassword) throws DBException;

    /**
     * Deletes user from table by id
     *
     * @param id user identifier
     */
    void deleteById(int id) throws DBException;

    /**
     * Gets user by login
     *
     * @param login users login
     * @return null if there isn't user with this login
     * or user object
     */
    User findUserByLogin(String login) throws DBException;

    /**
     * Change user role in db
     *
     * @param userId id of user
     * @param roleId new role id
     */
    void changeRole(int userId, int roleId) throws DBException;
}
