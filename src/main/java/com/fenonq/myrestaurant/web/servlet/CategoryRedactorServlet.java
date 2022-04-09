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
import java.util.List;

@WebServlet("/account/category-redactor")
public class CategoryRedactorServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(CategoryRedactorServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Locales locale = Locales.valueOf(req.getSession().getAttribute("locale").toString());
            List<Category> categories = DaoFactory.getInstance().getCategoryDao().findAll(locale);
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/WEB-INF/jsp/category_redactor.jsp").forward(req, resp);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = Integer.parseInt(req.getParameter("action"));
        Locales[] locales = (Locales[]) req.getSession().getAttribute("localesArr");
        try {
            int categoryId;
            Category category;
            switch (action) {
                case -1:
                    categoryId = Integer.parseInt(req.getParameter("categoryId"));
                    DaoFactory.getInstance().getCategoryDao().deleteById(categoryId);

                    resp.sendRedirect(req.getContextPath() + "/account/category-redactor");
                    break;

                case 0:
                    categoryId = Integer.parseInt(req.getParameter("categoryId"));

                    category = DaoFactory.getInstance().getCategoryDao().findById(categoryId, Locales.EN);
                    req.getSession().setAttribute("category", category);
                    req.getSession().setAttribute("categoryId", categoryId);

                    resp.sendRedirect(req.getContextPath() + "/account/category-redactor/update-category");
                    break;

                case 1:
                    String[] nameLocalization = new String[locales.length];
                    for (int i = 0; i < nameLocalization.length; i++) {
                        nameLocalization[i] = req.getParameter("name_" + locales[i].toString().toLowerCase());
                    }
                    category = new Category(nameLocalization);
                    DaoFactory.getInstance().getCategoryDao().create(category);
                    resp.sendRedirect(req.getContextPath() + "/account/category-redactor");
                    break;
            }
        } catch (DBException | IllegalArgumentException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
