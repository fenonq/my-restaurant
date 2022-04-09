package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
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


@WebServlet("/account/dish-redactor/update-dish")
public class UpdateDishServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UpdateDishServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/update_dish.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Locales[] locales = (Locales[]) req.getSession().getAttribute("localesArr");

        try {
            int dishId = Integer.parseInt(req.getSession().getAttribute("dishId").toString());

            int price = Integer.parseInt(req.getParameter("price"));
            int weight = Integer.parseInt(req.getParameter("weight"));
            int categoryId = Integer.parseInt(req.getParameter("category"));
            int isVisible = DaoFactory.getInstance().getDishDao().findById(dishId, Locales.EN).getIsVisible();

            SubDish subDish = new SubDish(price, weight, categoryId, isVisible);
            subDish.setId(dishId);

            DishDescription[] dishDescriptions = new DishDescription[locales.length];
            for (int i = 0; i < dishDescriptions.length; i++) {
                String name = req.getParameter("name_" + locales[i].toString().toLowerCase());
                String description = req.getParameter("description_" + locales[i].toString().toLowerCase());

                dishDescriptions[i] = new DishDescription(locales[i].getId(), name, description);
                dishDescriptions[i].setDishId(dishId);
            }

            DaoFactory.getInstance().getDishDao().update(subDish, dishDescriptions);

        } catch (DBException | IllegalArgumentException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/account/dish-redactor");
    }
}
