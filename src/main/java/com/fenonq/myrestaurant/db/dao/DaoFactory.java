package com.fenonq.myrestaurant.db.dao;

import com.fenonq.myrestaurant.db.dao.implementation.*;
import com.fenonq.myrestaurant.db.dao.interfaces.*;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public CategoryDao getCategoryDao() {
        return new CategoryDaoImpl();
    }

    public DishDao getDishDao() {
        return new DishDaoImpl();
    }

    public ReceiptDao getReceiptDao() {
        return new ReceiptDaoImpl();
    }

    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

    public UserCartDao getUserCartDao() {
        return new UserCartDaoImpl();
    }
}
