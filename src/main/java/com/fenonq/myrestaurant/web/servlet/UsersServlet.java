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
import java.util.List;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(UsersServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = DaoFactory.getInstance().getUserDao().findAll();
            users.removeIf(user -> user.getId() == ((User) req.getSession().getAttribute("user")).getId());
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        int roleId = Integer.parseInt(req.getParameter("roleId"));
        try {
            if (roleId == -1) {
                DaoFactory.getInstance().getUserDao().deleteById(userId);
            } else {
                DaoFactory.getInstance().getUserDao().changeRole(userId, roleId);
            }
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }
}
