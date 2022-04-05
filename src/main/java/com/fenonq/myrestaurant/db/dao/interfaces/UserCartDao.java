package com.fenonq.myrestaurant.db.dao.interfaces;

import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.DBException;

import java.util.Map;

public interface UserCartDao {

    /**
     * Add user_cart to db
     *
     * @param userId user unique identifier
     * @param dishId dish unique identifier to add
     * @param count count of dishes
     */
    void addDishToCart(int userId, int dishId, int count) throws DBException;

    /**
     * Get user_cart of user from db
     *
     * @param userId user unique identifier
     * @return Map(Dish, count)
     */
    Map<Dish, Integer> getUserCart(int userId, Locales locale) throws DBException;

    /**
     * Make receipt in db and add receipt_has_dish for every dish in cart
     *
     * @param userId user unique identifier
     * @param cart list of dishes to add
     */
    void makeAnOrder(int userId, Map<Dish, Integer> cart) throws DBException;

    /**
     * Remove one object of user_cart
     *
     * @param userId user unique identifier
     * @param dishId dish unique identifier
     */
    void removeDishFromCart(int userId, int dishId) throws DBException;

    /**
     * Remove all user_cart of user
     *
     * @param userId user unique identifier
     */
    void cleanUserCart(int userId) throws DBException;
}
