package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Dish;
import com.fenonq.myrestaurant.db.entity.Receipt;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(AccountServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("logout") != null) {
            logout(req, resp);
            return;
        }
        User user = (User) req.getSession().getAttribute("user");
        Locales locale = Locales.valueOf(req.getSession().getAttribute("locale").toString());
        try {
            List <Receipt> receipts = DaoFactory.getInstance().getReceiptDao()
                    .findUserReceipts(user.getId(), locale);
            req.getSession().setAttribute("receipts", receipts);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/account.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Map<Dish, Integer> cart = (Map<Dish, Integer>) session.getAttribute("cart");
        try {
            DaoFactory.getInstance().getUserCartDao().makeAnOrder(user.getId(), cart);
            DaoFactory.getInstance().getUserCartDao().cleanUserCart(user.getId());
            session.removeAttribute("cart");
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/account");
    }

    public static void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/account/login");
    }
}
