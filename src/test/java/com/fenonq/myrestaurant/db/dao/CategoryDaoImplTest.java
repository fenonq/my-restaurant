package com.fenonq.myrestaurant.db.dao;
import com.fenonq.myrestaurant.db.dao.implementation.CategoryDaoImpl;
import com.fenonq.myrestaurant.db.dao.util.DirectConnection;
import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
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

class CategoryDaoImplTest {

    private static Connection connection;
    private static CategoryDaoImpl categoryDao;

    @BeforeAll
    static void globalSetUp() throws SQLException {
        categoryDao = new CategoryDaoImpl();
        categoryDao.setConnectionBuilder(new DirectConnection());

        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        connection.createStatement().executeUpdate(CREATE_DATABASE);
        connection.createStatement().executeUpdate(USE_DATABASE);
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate(CREATE_TABLE_CATEGORY);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_TABLE_CATEGORY);
    }

    @AfterAll
    static void globalTearDown() throws SQLException {
        connection.createStatement().executeUpdate(DROP_DATABASE);
    }

    @Test
    void testConnection(){
        assertNotNull(connection);
    }

    @Test
    void testFindAll() throws DBException {
        assertEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
        List<Category> categoryTestList = getTestCategories(10);

        for (Category category: categoryTestList) {
            categoryDao.create(category);
        }

        assertNotEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
        assertEquals(categoryTestList.size(), categoryDao.findAll(Locales.EN).size());
    }

    @Test
    void testFindById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
        Category category = getTestCategories(1).get(0);
        categoryDao.create(category);
        assertEquals(1, categoryDao.findAll(Locales.EN).size());
        assertEquals(category, categoryDao.findById(1, Locales.EN));
    }

    @Test
    void testFindByIdNull() throws DBException {
        assertNull(categoryDao.findById(0, Locales.EN));
    }

    @Test
    void testCreate() throws DBException {
        assertEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
        List<Category> categoryTestList = getTestCategories(5);

        for (int i = 0; i < categoryTestList.size(); i++) {
            assertEquals(i, categoryDao.findAll(Locales.EN).size());
            categoryDao.create(categoryTestList.get(i));
            assertEquals(i + 1, categoryDao.findAll(Locales.EN).size());
        }

        assertEquals(categoryTestList.size(), categoryDao.findAll(Locales.EN).size());
    }

    @Test
    void testCreateError() throws DBException {
        Category category = getTestCategories(1).get(0);
        categoryDao.create(category);
        assertEquals(1, categoryDao.findAll(Locales.EN).size());
        assertThrows(DBException.class, ()-> categoryDao.create(category));
    }

    @Test
    void testUpdate() throws DBException {
        Category category = getTestCategories(1).get(0);

        categoryDao.create(category);
        assertEquals(1, categoryDao.findAll(Locales.EN).size());
        assertEquals(category, categoryDao.findById(category.getId(), Locales.EN));

        String updatedName = "UpdatedName";
        category.setNameEn(updatedName);

        categoryDao.update(category);
        assertEquals(1, categoryDao.findAll(Locales.EN).size());
        assertEquals(updatedName, categoryDao.findById(category.getId(), Locales.EN).getNameEn());
    }

    @Test
    void testUpdateError() throws DBException {
        List<Category> categoryTestList = getTestCategories(2);

        for (Category category: categoryTestList) {
            categoryDao.create(category);
        }

        assertEquals(categoryTestList.size(), categoryDao.findAll(Locales.EN).size());
        assertEquals(categoryTestList.get(0), categoryDao.findById(categoryTestList.get(0).getId(), Locales.EN));

        categoryTestList.get(1).setId(categoryTestList.get(0).getId());
        assertThrows(DBException.class, ()-> categoryDao.update(categoryTestList.get(1)));
    }

    @Test
    void testDeleteById() throws DBException {
        assertEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
        List<Category> categoryTestList = getTestCategories(5);

        for (int i = 0; i < categoryTestList.size(); i++) {
            assertEquals(i, categoryDao.findAll(Locales.EN).size());
            categoryDao.create(categoryTestList.get(i));
            assertEquals(i + 1, categoryDao.findAll(Locales.EN).size());
        }
        assertEquals(categoryTestList.size(), categoryDao.findAll(Locales.EN).size());

        for (int i = categoryTestList.size() - 1 ; i >= 0; i--) {
            assertEquals(i + 1, categoryDao.findAll(Locales.EN).size());
            categoryDao.deleteById(categoryTestList.get(i).getId());
            assertEquals(i, categoryDao.findAll(Locales.EN).size());
        }
        assertEquals(Collections.EMPTY_LIST, categoryDao.findAll(Locales.EN));
    }

    public static List<Category> getTestCategories(int count) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Category category = new Category(new String[]{"Test" + i, "Тест" + i});
            categories.add(category);
        }
        return categories;
    }
}