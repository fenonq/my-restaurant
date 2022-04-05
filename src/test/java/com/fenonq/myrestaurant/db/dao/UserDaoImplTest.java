package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.dao.implementation.UserDaoImpl;
import com.fenonq.myrestaurant.db.dao.util.DirectConnection;
import com.fenonq.myrestaurant.db.entity.User;
import com.fenonq.myrestaurant.exception.DBException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fenonq.myrestaurant.db.dao.util.TestDBTools.*;
import static org.junit.jupiter.api.Assertions.*;


class UserDaoImplTest {

    private static Connection connection;
    private static UserDaoImpl userDao;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        userDao = new UserDaoImpl();
        userDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_USER);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_USER);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_DATABASE);
    }

    @Test
    void testConnection() {
        assertNotNull(connection);
    }

    @Test
    void testFindAll() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
        List<User> userTestList = getTestUsers(10);

        for (User user : userTestList) {
            userDao.signUp(user);
        }

        assertNotEquals(Collections.EMPTY_LIST, userDao.findAll());
        assertEquals(userTestList.size(), userDao.findAll().size());
    }

    @Test
    void testFindById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
        User user = getTestUsers(1).get(0);
        userDao.signUp(user);
        assertEquals(1, userDao.findAll().size());
        assertEquals(user, userDao.findById(user.getId()));
    }

    @Test
    void testFindByIdNull() throws DBException {
        assertNull(userDao.findById(0));
    }

    @Test
    void testFindByLogin() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
        User user = getTestUsers(1).get(0);
        userDao.signUp(user);
        assertEquals(1, userDao.findAll().size());
        assertEquals(user, userDao.findUserByLogin(user.getLogin()));
    }

    @Test
    void testFindByLoginNull() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
        assertNull(userDao.findUserByLogin("Test"));
    }

    @Test
    void testSignUp() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
        List<User> userTestList = getTestUsers(5);

        for (int i = 0; i < userTestList.size(); i++) {
            assertEquals(i, userDao.findAll().size());
            userDao.signUp(userTestList.get(i));
            assertEquals(i + 1, userDao.findAll().size());
        }

        assertEquals(userTestList.size(), userDao.findAll().size());
    }

    @Test
    void testSignUpError() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());

        User user = getTestUsers(1).get(0);
        userDao.signUp(user);

        assertEquals(user, userDao.findById(user.getId()));
        assertThrows(DBException.class, () -> userDao.signUp(user));
    }

    @Test
    void testLogIn() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());

        User user = getTestUsers(1).get(0);
        String login = user.getLogin();
        String password = user.getPassword();
        userDao.signUp(user);

        assertEquals(user, userDao.logIn(login, password));
    }

    @Test
    void testLogInNull() throws DBException {
        assertNull(userDao.logIn("Test", "Test"));
    }

    @Test
    void testChangePassword() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());

        User user = getTestUsers(1).get(0);

        userDao.signUp(user);
        String oldPassword = user.getPassword();
        assertEquals(oldPassword, userDao.findById(user.getId()).getPassword());

        userDao.changePassword(user.getId(), "newPassword");
        assertNotEquals(oldPassword, userDao.findById(user.getId()).getPassword());
    }

    @Test
    void testDeleteById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());

        User user = getTestUsers(1).get(0);
        userDao.signUp(user);
        assertEquals(user, userDao.findById(user.getId()));

        userDao.deleteById(user.getId());
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());
    }

    @Test
    void testChangeRole() throws DBException {
        assertEquals(Collections.EMPTY_LIST, userDao.findAll());

        User user = getTestUsers(1).get(0);
        userDao.signUp(user);

        int oldRole = user.getRoleId();
        assertEquals(oldRole, userDao.findById(user.getId()).getRoleId());

        int newRole = 1;
        userDao.changeRole(user.getId(), newRole);

        assertEquals(newRole, userDao.findById(user.getId()).getRoleId());
        assertNotEquals(newRole, oldRole);
    }

    public static List<User> getTestUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User("Name" + i, "Surname" + i, "Login" + i, "Password" + i);
            users.add(user);
        }
        return users;
    }
}