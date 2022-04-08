package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.dishlocalization.DishDescription;
import com.fenonq.myrestaurant.db.entity.dishlocalization.SubDish;
import com.fenonq.myrestaurant.db.entity.enums.Locales;
import com.fenonq.myrestaurant.exception.AppException;
import com.fenonq.myrestaurant.exception.DBException;
import com.fenonq.myrestaurant.validation.Validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/account/dish-redactor")
public class DishRedactorServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(DishRedactorServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Locales locale = Locales.valueOf(req.getSession().getAttribute("locale").toString());

            List<Category> categories = DaoFactory.getInstance().getCategoryDao().findAll(locale);
            req.getSession().setAttribute("categories", categories);

            List<Dish> dishes = DaoFactory.getInstance().getDishDao().findAll(locale);
            req.setAttribute("dishes", dishes);

            req.getRequestDispatcher("/WEB-INF/jsp/dish_redactor.jsp").forward(req, resp);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = Integer.parseInt(req.getParameter("action"));
        Locales [] locales = (Locales[]) req.getSession().getAttribute("localesArr");
        try {
            int dishId;
            SubDish subDish;
            switch (action) {
                case -1:
                    dishId = Integer.parseInt(req.getParameter("dishId"));
                    int isVisible = Integer.parseInt(req.getParameter("isVisible"));

                    DaoFactory.getInstance().getDishDao().changeStatus(dishId, isVisible);
                    resp.sendRedirect(req.getContextPath() + "/account/dish-redactor");
                    break;

                case 0:
                    dishId = Integer.parseInt(req.getParameter("dishId"));

                    List<DishDescription> dishDescriptionsList = DaoFactory.getInstance().getDishDao().findLocalizationById(dishId);
                    Map<Integer, DishDescription> dishDescriptionMap = new HashMap<>();

                    for (DishDescription el: dishDescriptionsList) {
                        dishDescriptionMap.put(el.getLanguageId(), el);
                    }

                    subDish = DaoFactory.getInstance().getDishDao().findSubDishById(dishId);
                    req.getSession().setAttribute("dishDescriptionMap", dishDescriptionMap);
                    req.getSession().setAttribute("subDish", subDish);
                    req.getSession().setAttribute("dishId", dishId);

                    resp.sendRedirect(req.getContextPath() + "/account/dish-redactor/update-dish");
                    break;

                case 1:
                    int price = Validation.validatePositiveNumber(Integer.parseInt(req.getParameter("price")));
                    int weight = Validation.validatePositiveNumber(Integer.parseInt(req.getParameter("weight")));
                    int categoryId = Integer.parseInt(req.getParameter("category"));

                    subDish = new SubDish(price, weight, categoryId, 1);

                    DishDescription [] dishDescriptions = new DishDescription[locales.length];
                    for (int i = 0; i < dishDescriptions.length; i++) {
                        String name = req.getParameter("name_" + locales[i].toString().toLowerCase());
                        String description = req.getParameter("description_" + locales[i].toString().toLowerCase());
                        dishDescriptions[i] = new DishDescription(locales[i].getId(), name, description);
                    }

                    DaoFactory.getInstance().getDishDao().createDish(subDish, dishDescriptions);
                    resp.sendRedirect(req.getContextPath() + "/account/dish-redactor");
                    break;
            }
        } catch (DBException | IllegalArgumentException e) {
            req.getSession().setAttribute("errorMsg", e.getMessage());
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
