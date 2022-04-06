package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Category;
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


@WebServlet("/account/category-redactor/update-category")
public class UpdateCategoryServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UpdateCategoryServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/update_category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Locales[] locales = (Locales[]) req.getSession().getAttribute("localesArr");

        try {
            int categoryId = Integer.parseInt(req.getSession().getAttribute("categoryId").toString());

            Category category = DaoFactory.getInstance().getCategoryDao().findById(categoryId, Locales.EN);
            req.getSession().setAttribute("category", category);

            String[] nameLocalization = new String[locales.length];
            for (int i = 0; i < nameLocalization.length; i++) {
                nameLocalization[i] = req.getParameter("name_" + locales[i].toString().toLowerCase());
            }

            Category newCategory = new Category(nameLocalization);
            newCategory.setId(categoryId);

            DaoFactory.getInstance().getCategoryDao().update(newCategory);

            req.getSession().setAttribute("category", newCategory);
        } catch (DBException | IllegalArgumentException e) {
            req.getSession().setAttribute("errorMsg", e.getMessage());
            log.error("Error: " + e);
            throw new AppException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/account/category-redactor");
    }
}

