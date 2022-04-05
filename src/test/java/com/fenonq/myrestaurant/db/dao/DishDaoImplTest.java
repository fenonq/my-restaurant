package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.dao.implementation.DishDaoImpl;
import com.fenonq.myrestaurant.db.dao.util.DirectConnection;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static com.fenonq.myrestaurant.db.dao.util.TestDBTools.*;
import static org.junit.jupiter.api.Assertions.*;


class DishDaoImplTest {

    private static Connection connection;
    private static DishDaoImpl dishDao;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        dishDao = new DishDaoImpl();
        dishDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH);
        connection.createStatement().executeUpdate(CREATE_TABLE_DISH_DESCRIPTION);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_DISH_DESCRIPTION);
        connection.createStatement().executeUpdate(DROP_TABLE_DISH);
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
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(2);

        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());
    }

    @Test
    void testFindById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(2);

        for (SubDish subDish : dishTestMap.keySet()) {
            DishDescription [] dishDescriptionsArray = dishTestMap.get(subDish);
            List<DishDescription> dishDescriptionsList = Arrays.asList(dishDescriptionsArray);

            dishDao.createDish(subDish, dishDescriptionsArray);

            assertEquals(subDish.getId(), dishDao.findById(subDish.getId(), Locales.EN).getId());
            assertEquals(subDish, dishDao.findSubDishById(subDish.getId()));
            assertEquals(dishDescriptionsList, dishDao.findLocalizationById(subDish.getId()));
        }
    }

    @Test
    void testFindByIdNull() throws DBException {
        assertNull(dishDao.findById(0, Locales.EN));
    }

    @Test
    void testCreate() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);

        int i = 0;
        for (SubDish subDish : dishTestMap.keySet()) {
            assertEquals(i, dishDao.findAll(Locales.EN).size());
            dishDao.createDish(subDish, dishTestMap.get(subDish));
            assertEquals(++i, dishDao.findAll(Locales.EN).size());
        }

        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());
    }

    @Test
    void testCreateError() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(1);

        for (SubDish subDish : dishTestMap.keySet()) {
            DishDescription[] dishDescriptions = dishTestMap.get(subDish);
            dishDao.createDish(subDish, dishDescriptions);
            assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());
            assertThrows(DBException.class, () -> dishDao.createDish(subDish, dishDescriptions));
        }
    }

    @Test
    void testUpdate() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(1);
        String updatedName = "UpdatedName";

        SubDish subDish = (SubDish) dishTestMap.keySet().toArray()[0];
        DishDescription[] dishDescriptions = dishTestMap.get(subDish);
        dishDao.createDish(subDish, dishDescriptions);
        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());

        dishDescriptions[1].setName(updatedName);
        dishDao.update(subDish, dishDescriptions);

        assertEquals(dishDescriptions[1].getName(), dishDao.findById(subDish.getId(), Locales.EN).getName());
    }

    @Test
    void testUpdateError() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(1);

        SubDish subDish = (SubDish) dishTestMap.keySet().toArray()[0];
        DishDescription[] dishDescriptions = dishTestMap.get(subDish);
        dishDao.createDish(subDish, dishDescriptions);

        dishDescriptions[1].setName(dishDescriptions[0].getName());
        assertThrows(DBException.class, () -> dishDao.update(subDish, dishDescriptions));

    }

    @Test
    void testDeleteById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);

        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }

        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());

        int i = dishTestMap.size();
        for (SubDish subDish : dishTestMap.keySet()) {
            assertEquals(i, dishDao.findAll(Locales.EN).size());
            dishDao.deleteById(subDish.getId());
            assertEquals(--i, dishDao.findAll(Locales.EN).size());
        }

        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
    }

    @Test
    void testFindSortedDishesOnPage() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(6);

        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());

        int dishesOnPage = 2;
        for (int i = 0; i < dishDao.getDishesNumber() / dishesOnPage; i++) {
            assertEquals(dishesOnPage,
                    dishDao.findSortedDishesOnPage(Locales.EN, "category_id", dishesOnPage, i).size());
        }
        assertEquals(dishDao.getDishesNumber(),
                dishDao.findSortedDishesOnPage(Locales.EN, "category_id",
                        dishDao.getDishesNumber(), 0).size());
    }

    @Test
    void testFindSortedDishesOnPageError() throws DBException {
        assertThrows(DBException.class, () -> dishDao.findSortedDishesOnPage(Locales.EN, "wrong_column",
                dishDao.getDishesNumber(), 0));
    }

    @Test
    void testFindSortedByCategoryIdOnPage() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(6);

        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
        }
        assertEquals(dishTestMap.size(), dishDao.findAll(Locales.EN).size());

        for (int i = 0; i < dishTestMap.size(); i++) {
            assertEquals(dishDao.getDishesNumberInCategory(i),
                    dishDao.findSortedByCategoryIdOnPage(Locales.EN, "category_id",
                            i, dishDao.getDishesNumberInCategory(i), 0).size());
        }
    }

    @Test
    void testFindSortedByCategoryIdOnPageError() throws DBException {
        assertThrows(DBException.class, () -> dishDao.findSortedByCategoryIdOnPage(Locales.EN, "wrong_column",
                0, 0, 0));
    }

    @Test
    void testGetDishesNumber() throws DBException {
        assertEquals(0, dishDao.getDishesNumber());
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);

        int i = 0;
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
            assertEquals(++i, dishDao.getDishesNumber());
        }
        assertEquals(dishTestMap.size(), dishDao.getDishesNumber());
    }

    @Test
    void testGetDishesNumberError() throws DBException {
        assertEquals(0, dishDao.getDishesNumber());
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);

        int i = 0;
        for (SubDish subDish : dishTestMap.keySet()) {
            dishDao.createDish(subDish, dishTestMap.get(subDish));
            assertEquals(++i, dishDao.getDishesNumber());
        }
        assertEquals(dishTestMap.size(), dishDao.getDishesNumber());
    }

    @Test
    void testGetDishesNumberInCategory() throws DBException {
        assertEquals(Collections.EMPTY_LIST, dishDao.findAll(Locales.EN));
        Map<SubDish, DishDescription[]> dishTestMap = getTestDishes(5);

        int categoryId = 1;
        for (SubDish subDish : dishTestMap.keySet()) {
            DishDescription[] dishDescriptions = dishTestMap.get(subDish);
            subDish.setCategoryId(categoryId);
            dishDao.createDish(subDish, dishDescriptions);
        }
        assertEquals(dishTestMap.size(), dishDao.getDishesNumberInCategory(categoryId));
    }

    public static Map<SubDish, DishDescription[]> getTestDishes(int count) {
        Map<SubDish, DishDescription[]> dishes = new HashMap<>();

        for (int i = 0; i < count; i++) {
            SubDish subDish = new SubDish(100 + i, 100 + i, 1 + i);
            DishDescription[] dishDescriptions = new DishDescription[Locales.values().length];
            for (int j = 0; j < Locales.values().length; j++) {
                dishDescriptions[j] = new DishDescription(j, "TestName" + i + j, "TestDescription" + i + j);
            }
            dishes.put(subDish, dishDescriptions);
        }
        return dishes;
    }
}