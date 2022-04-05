package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.util.List;

public interface DishDao extends Dao<Dish> {

    /**
     * Updates dish in the table
     *
     * @param dish            SubDish to update
     * @param dishDescription array of DishDescription to update
     */

    void update(SubDish dish, DishDescription... dishDescription) throws DBException;

    /**
     * Creates dish in the table
     *
     * @param subDish          object to create
     * @param dishDescriptions localizations of the dish
     */
    void createDish(SubDish subDish, DishDescription... dishDescriptions) throws DBException;

    /**
     * Finds dishes by category
     *
     * @param categoryId   category unique identifier
     * @param dishesInPage number of dishes in page
     * @param pageNum      page number
     * @return List of dishes
     */
    List<Dish> findSortedByCategoryIdOnPage(Locales locale, String sortBy, int categoryId, int dishesInPage, int pageNum) throws DBException;

    /**
     * Finds sorted dishes
     *
     * @param sortBy       name of field in dish table to sort by
     * @param dishesInPage number of dishes in page
     * @param pageNum      page number
     * @return List of dishes
     */
    List<Dish> findSortedDishesOnPage(Locales locale, String sortBy, int dishesInPage, int pageNum) throws DBException;

    /**
     * Counts the number of dishes
     *
     * @return number of dishes
     */
    int getDishesNumber() throws DBException;

    /**
     * Counts the number of dishes in category
     *
     * @param categoryId category unique identifier
     * @return number of dishes
     */
    int getDishesNumberInCategory(int categoryId) throws DBException;

    /**
     * Finds DishLocalization by dish id
     *
     * @param id unique dish identifier
     * @return List of DishDescription
     */
    List<DishDescription> findLocalizationById(int id) throws DBException;

    /**
     * Finds SubDish by dish id
     *
     * @param id unique dish identifier
     * @return SubDish object
     */
    SubDish findSubDishById(int id) throws DBException;
}
