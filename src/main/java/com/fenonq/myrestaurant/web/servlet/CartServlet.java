package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.User;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.AppException;
import com.fenonq.myrestaurant.exception.DBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CartServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locale = req.getSession().getAttribute("locale").toString();
        int totalPrice = 0;

        Map<Dish, Integer> cart = new HashMap<>();

        if (req.getSession().getAttribute("user") == null) {
            if (req.getSession().getAttribute("cart") == null) {
                req.getSession().setAttribute("cart", cart);
            } else {
                cart = (Map<Dish, Integer>) req.getSession().getAttribute("cart");
            }
        } else {
            User user = (User) req.getSession().getAttribute("user");
            if (user.getRoleId() != 1) {
                try {
                    Map<Dish, Integer> sessionCart;
                    cart = DaoFactory.getInstance().getUserCartDao().getUserCart(user.getId(), Locales.valueOf(locale));
                    sessionCart = (Map<Dish, Integer>) req.getSession().getAttribute("cart");
                    if (sessionCart != null) {
                        for (Map.Entry<Dish, Integer> entry : sessionCart.entrySet()) {
                            if (cart.containsKey(entry.getKey())) {
                                continue;
                            }

                            if (DaoFactory.getInstance().getDishDao().findById(entry.getKey().getId(), Locales.EN) != null) {
                                DaoFactory.getInstance().getUserCartDao()
                                        .addDishToCart(user.getId(), entry.getKey().getId(), entry.getValue());

                                cart.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                    req.getSession().setAttribute("cart", cart);
                } catch (DBException e) {
                    log.error("Error: " + e);
                    throw new AppException(e);
                }
            } else {
                req.getSession().removeAttribute("cart");
            }
        }

        for (Map.Entry<Dish, Integer> entry : cart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        req.getSession().setAttribute("totalPrice", totalPrice);
        req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int dishId = Integer.parseInt(req.getParameter("id"));
        int count = Integer.parseInt(req.getParameter("count"));
        String locale = req.getSession().getAttribute("locale").toString();

        Map<Dish, Integer> cart;
        try {

            Dish dish = DaoFactory.getInstance().getDishDao().findById(dishId, Locales.valueOf(locale));

            if (req.getSession().getAttribute("user") == null) {
                if (req.getSession().getAttribute("cart") != null) {
                    cart = new HashMap<>((Map<Dish, Integer>) req.getSession().getAttribute("cart"));
                } else {
                    cart = new HashMap<>();
                }
                if (count == -1) {
                    cart.remove(dish);
                } else {
                    cart.put(dish, count);
                }
                req.getSession().setAttribute("cart", cart);
            } else {
                User user = (User) req.getSession().getAttribute("user");
                if (count == -1) {
                    DaoFactory.getInstance().getUserCartDao().removeDishFromCart(user.getId(), dishId);
                } else {
                    DaoFactory.getInstance().getUserCartDao().addDishToCart(user.getId(), dishId, count);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}