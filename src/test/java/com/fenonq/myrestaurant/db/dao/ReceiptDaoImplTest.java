package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.dao.implementation.ReceiptDaoImpl;
import com.fenonq.myrestaurant.db.dao.util.DirectConnection;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

import static com.fenonq.myrestaurant.db.dao.util.TestDBTools.*;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptDaoImplTest {

    private static Connection connection;
    private static ReceiptDaoImpl receiptDao;
    private static int countOfReceipts = 0;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        receiptDao = new ReceiptDaoImpl();
        receiptDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH);
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH_DESCRIPTION);
        connection.createStatement().executeUpdate(CREATE_TABLE_RECEIPT);
        connection.createStatement().executeUpdate(CREATE_TABLE_STATUS);
        connection.createStatement().executeUpdate(CREATE_TABLE_RECEIPT_HAS_DISH);

        for (int i = 0; i < 5; i++) {
            connection.createStatement().executeUpdate(INSERT_INTO_STATUS(i));
        }

        countOfReceipts = 0;
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_DISH);
        connection.createStatement().executeUpdate(DROP_TABLE_DISH_DESCRIPTION);
        connection.createStatement().executeUpdate(DROP_TABLE_RECEIPT_HAS_DISH);
        connection.createStatement().executeUpdate(DROP_TABLE_STATUS);
        connection.createStatement().executeUpdate(DROP_TABLE_RECEIPT);
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
    void testFindAll() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        createReceipt(1, 2, 1);
        createReceipt(1, 2, 1);
        assertEquals(countOfReceipts, receiptDao.findAll(Locales.EN).size());
    }

    @Test
    void testFindById() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        int userId = 1;
        createReceipt(userId, 2, 1);
        assertEquals(userId, receiptDao.findById(userId, Locales.EN).getId());
    }

    @Test
    void testDeleteById() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        int userId = 1;
        createReceipt(userId, 2, 1);
        assertEquals(1, receiptDao.findById(userId, Locales.EN).getId());

        receiptDao.deleteById(userId);
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
    }

    @Test
    void testFindUserReceipts() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        createReceipt(1, 2, 1);
        createReceipt(1, 2, 1);
        createReceipt(3, 2, 1);
        assertEquals(2, receiptDao.findUserReceipts(1, Locales.EN).size());
    }

    @Test
    void testFindDishesByReceipt() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        createReceipt(1, 2, 1);
        createReceipt(1, 2, 1);
        createReceipt(3, 2, 1);
        assertEquals(2, receiptDao.findUserReceipts(1, Locales.EN).size());
    }

    @Test
    void testFindAllReceiptsAcceptedBy() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        createReceipt(1, 2, 1);
        createReceipt(1, 2, 1);
        createReceipt(3, 4, 1);
        assertEquals(2, receiptDao.findAllReceiptsAcceptedBy(2, Locales.EN).size());
    }

    @Test
    void testFindByStatusId() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        createReceipt(1, 2, 1);
        createReceipt(1, 2, 1);
        createReceipt(3, 4, 2);
        assertEquals(2, receiptDao.findByStatusId(1, Locales.EN).size());
    }

    @Test
    void testGetAllStatus() throws DBException {
        assertEquals(5, receiptDao.getAllStatus(Locales.EN).size());
    }

    @Test
    void testChangeStatus() throws DBException, SQLException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));
        int oldStatusId = 1;
        int newStatusId = 2;
        createReceipt(1, 2, oldStatusId);
        assertEquals(oldStatusId, receiptDao.findAll(Locales.EN).get(0).getStatus().getId());
        receiptDao.changeStatus(1, newStatusId, 2);
        assertNotEquals(oldStatusId, receiptDao.findAll(Locales.EN).get(0).getStatus().getId());
        assertEquals(newStatusId, receiptDao.findAll(Locales.EN).get(0).getStatus().getId());
    }

    public static void createReceipt(int userId, int managerId, int statusId) throws SQLException {
        connection.createStatement().executeUpdate(INSERT_INTO_RECEIPT(userId, managerId, statusId));
        countOfReceipts++;
    }
}
