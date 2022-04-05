package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.User;
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


@WebServlet("/account/login")
public class LogInServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(LogInServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = DaoFactory.getInstance().getUserDao().logIn(login, password);
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/account/login?err=");
            } else {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/menu");
            }
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
