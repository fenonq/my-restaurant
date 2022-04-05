package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.db.entity.Dish;
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
import java.util.List;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(MenuServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Locales locale = Locales.valueOf(req.getSession().getAttribute("locale").toString());
        req.getSession().setAttribute("orderByParams", Dish.orderByParams);

        int category = req.getParameter("category") == null ? 0 : Integer.parseInt(req.getParameter("category"));

        int page = req.getParameter("page") == null ? 0 : Integer.parseInt(req.getParameter("page"));

        int dishesOnPage = 0;
        try {
            dishesOnPage = req.getParameter("dishesOnPage") == null ? DaoFactory.getInstance().getDishDao().getDishesNumber() : Integer.parseInt(req.getParameter("dishesOnPage"));
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }

        String orderBy = req.getParameter("orderBy") == null ? "category_id" : req.getParameter("orderBy");

        req.getSession().setAttribute("page", page);
        try {

            List<Category> categories = DaoFactory.getInstance().getCategoryDao().findAll(locale);
            req.getSession().setAttribute("categories", categories);

            List<Dish> dishes;
            int dishNumber;
            if (category == 0) {
                dishes = DaoFactory.getInstance().getDishDao()
                        .findSortedDishesOnPage(locale, orderBy, dishesOnPage, page);
                dishNumber = DaoFactory.getInstance().getDishDao().getDishesNumber();
            } else {
                dishes = DaoFactory.getInstance().getDishDao()
                        .findSortedByCategoryIdOnPage(locale, orderBy, category, dishesOnPage, page);
                dishNumber = DaoFactory.getInstance().getDishDao().getDishesNumberInCategory(category);
            }
            int pages = dishNumber % dishesOnPage == 0 ? (dishNumber / dishesOnPage) - 1 : dishNumber / dishesOnPage;

            req.getSession().setAttribute("pages", pages);
            req.getSession().setAttribute("dishes", dishes);
            req.getRequestDispatcher("/WEB-INF/jsp/menu.jsp").forward(req, resp);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }

    }

}
