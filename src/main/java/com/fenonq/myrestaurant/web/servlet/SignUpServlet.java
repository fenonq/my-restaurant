package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.User;
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


@WebServlet("/account/signup")
public class SignUpServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(SignUpServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String name = Validation.validateName(req.getParameter("name"));
            String surname = Validation.validateName(req.getParameter("surname"));
            String login = Validation.validateLogin(req.getParameter("login"));
            String password = Validation.validatePassword(req.getParameter("password"));

            if (DaoFactory.getInstance().getUserDao().findUserByLogin(login) != null) {
                resp.sendRedirect(req.getContextPath() + "/account/signup?err=");
                return;
            }
            User user = DaoFactory.getInstance().getUserDao().signUp(new User(name, surname, login, password));
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/menu");
        } catch (DBException | IllegalArgumentException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
