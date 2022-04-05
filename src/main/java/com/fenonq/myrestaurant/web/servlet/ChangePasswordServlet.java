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

@WebServlet("/account/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(ChangePasswordServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/change_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String oldPass = req.getParameter("old-password");
            String newPass = Validation.validatePassword(req.getParameter("new-password"));
            User currentUser = (User) req.getSession().getAttribute("user");

            User user = DaoFactory.getInstance().getUserDao().logIn(currentUser.getLogin(), oldPass);
            if (user == null) {
                req.setAttribute("err", "true");
                resp.sendRedirect(req.getContextPath() + "/account/change-password?err=");
            } else {
                DaoFactory.getInstance().getUserDao().changePassword(user.getId(), newPass);
                resp.sendRedirect(req.getContextPath() + "/account");
            }
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
