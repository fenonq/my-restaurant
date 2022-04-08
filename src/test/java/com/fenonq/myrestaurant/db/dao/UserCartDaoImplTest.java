package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.dao.implementation.DishDaoImpl;
import com.fenonq.myrestaurant.db.dao.implementation.ReceiptDaoImpl;
import com.fenonq.myrestaurant.db.dao.implementation.UserCartDaoImpl;
import com.fenonq.myrestaurant.db.dao.util.DirectConnection;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.fenonq.myrestaurant.db.dao.util.TestDBTools.*;
import static org.junit.jupiter.api.Assertions.*;

class UserCartDaoImplTest {

    private static Connection connection;
    private static UserCartDaoImpl userCartDao;
    private static DishDaoImpl dishDao;
    private static ReceiptDaoImpl receiptDao;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        userCartDao = new UserCartDaoImpl();
        dishDao = new DishDaoImpl();
        receiptDao = new ReceiptDaoImpl();

        userCartDao.setConnectionBuilder(new DirectConnection());
        dishDao.setConnectionBuilder(new DirectConnection());
        receiptDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH);
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH_DESCRIPTION);
        connection.createStatement().executeUpdate(CREATE_TABLE_USER_CART);
        connection.createStatement().executeUpdate(CREATE_TABLE_RECEIPT);
        connection.createStatement().executeUpdate(CREATE_TABLE_RECEIPT_HAS_DISH);
        connection.createStatement().executeUpdate(CREATE_TABLE_STATUS);

        for (int i = 0; i < 5; i++) {
            connection.createStatement().executeUpdate(INSERT_INTO_STATUS(i));
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_STATUS);
        connection.createStatement().executeUpdate(DROP_TABLE_RECEIPT_HAS_DISH);
        connection.createStatement().executeUpdate(DROP_TABLE_RECEIPT);
        connection.createStatement().executeUpdate(DROP_TABLE_DISH);
        connection.createStatement().executeUpdate(DROP_TABLE_DISH_DESCRIPTION);
        connection.createStatement().executeUpdate(DROP_TABLE_USER_CART);
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
    void testGetUserCart() throws DBException {
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
        int countOfDishes = 5;
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(countOfDishes);
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        getTestUserCart(countOfDishes);
        assertEquals(countOfDishes, userCartDao.getUserCart(1, Locales.EN).size());
    }

    @Test
    void testAddDishToCart() throws DBException {
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
        int countOfDishes = 5;

        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(countOfDishes);
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }

        for (int i = 0; i < countOfDishes; i++) {
            userCartDao.addDishToCart(1, i + 1, 1);
            assertEquals(i + 1, userCartDao.getUserCart(1, Locales.EN).size());
        }
        getTestUserCart(countOfDishes);
        assertEquals(countOfDishes, userCartDao.getUserCart(1, Locales.EN).size());
    }

    @Test
    void testRemoveDishFromCart() throws DBException {
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
        int countOfDishes = 5;
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        getTestUserCart(countOfDishes);
        assertEquals(countOfDishes, userCartDao.getUserCart(1, Locales.EN).size());

        for (int i = 0, count = countOfDishes - 1; i < countOfDishes; i++, count--) {
            userCartDao.removeDishFromCart(1, i + 1);
            assertEquals(count, userCartDao.getUserCart(1, Locales.EN).size());
        }
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
    }

    @Test
    void testCleanUserCart() throws DBException {
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
        int countOfDishes = 5;
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        getTestUserCart(countOfDishes);
        assertEquals(countOfDishes, userCartDao.getUserCart(1, Locales.EN).size());

        userCartDao.cleanUserCart(1);
        assertEquals(Collections.EMPTY_MAP, userCartDao.getUserCart(1, Locales.EN));
    }

    @Test
    void testMakeAnOrder() throws DBException {
        assertEquals(Collections.EMPTY_LIST, receiptDao.findAll(Locales.EN));

        int countOfDishes = 5;
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        getTestUserCart(countOfDishes);
        assertEquals(countOfDishes, userCartDao.getUserCart(1, Locales.EN).size());

        userCartDao.makeAnOrder(1, userCartDao.getUserCart(1, Locales.EN));

        assertEquals(1, receiptDao.findUserReceipts(1, Locales.EN).size());
    }

    public static Map<SubDish, DishDescription[]> getTestDishes(int count) {
        Map<SubDish, DishDescription[]> dishes = new HashMap<>();

        for (int i = 0; i < count; i++) {
            SubDish subDish = new SubDish(100 + i, 100 + i, 1 + i, 1);
            DishDescription[] dishDescriptions = new DishDescription[Locales.values().length];
            for (int j = 0; j < Locales.values().length; j++) {
                dishDescriptions[j] = new DishDescription(j, "TestName" + i + j, "TestDescription" + i + j);
            }
            dishes.put(subDish, dishDescriptions);
        }
        return dishes;
    }

    public static void getTestUserCart(int count) throws DBException {
        for (int i = 0; i < count; i++) {
            userCartDao.addDishToCart(1, i + 1, 1);
        }
    }
}