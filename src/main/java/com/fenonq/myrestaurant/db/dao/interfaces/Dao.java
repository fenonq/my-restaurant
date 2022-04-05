package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.util.List;

/**
 * Interface to read and delete localization objects
 *
 * @param <T>
 */
public interface Dao<T> {
    /**
     * Finds all objects stored in the table
     *
     * @return List of T objects
     */
    List<T> findAll(Locales locale) throws DBException;

    /**
     * Finds object by id
     *
     * @param id unique entity identifier
     * @return T object
     */
    T findById(int id, Locales locale) throws DBException;

    /**
     * Deletes object from table by id
     *
     * @param id entity identifier
     */
    void deleteById(int id) throws DBException;
}
