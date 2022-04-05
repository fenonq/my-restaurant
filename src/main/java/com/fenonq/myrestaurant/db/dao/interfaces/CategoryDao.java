package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.exception.DBException;

public interface CategoryDao extends Dao<Category> {
    /**
     * Creates object in the table
     *
     * @param category object to create
     */
    void create(Category category) throws DBException;

    /**
     * Updates category in the table
     *
     * @param category Category to update
     */
    void update(Category category) throws DBException;
}
