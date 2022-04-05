package com.fenonq.myrestaurant.web.servlet;

import com.fenonq.myrestaurant.db.dao.DaoFactory;
import com.fenonq.myrestaurant.db.entity.Category;
import com.fenonq.myrestaurant.db.entity.Receipt;
import com.fenonq.myrestaurant.db.entity.Status;
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
import java.io.IOException;
import java.util.List;

@WebServlet("/receipts")
public class ReceiptsServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(ReceiptsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Locales locale = Locales.valueOf(req.getSession().getAttribute("locale").toString());

        int statusId = req.getParameter("statusId") == null ? 0 : Integer.parseInt(req.getParameter("statusId"));
        User user = (User) req.getSession().getAttribute("user");
        try {
            List<Status> statuses = DaoFactory.getInstance().getReceiptDao().getAllStatus(locale);
            req.getSession().setAttribute("statuses", statuses);

            List<Receipt> receipts;
            if (statusId == 0) {
                receipts = DaoFactory.getInstance().getReceiptDao().findAll(locale);
            } else if (statusId == statuses.size() + 1) {
                receipts = DaoFactory.getInstance().getReceiptDao().findAllReceiptsAcceptedBy(user.getId(), locale);
            } else {
                receipts = DaoFactory.getInstance().getReceiptDao().findByStatusId(statusId, locale);
            }

            req.getSession().setAttribute("receipts", receipts);
            req.getRequestDispatcher("/WEB-INF/jsp/receipts.jsp").forward(req, resp);
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int receiptId = Integer.parseInt(req.getParameter("id"));
        int newStatusId = Integer.parseInt(req.getParameter("statusId"));
        try {
            DaoFactory.getInstance().getReceiptDao().changeStatus(receiptId, newStatusId, user.getId());
        } catch (DBException e) {
            log.error("Error: " + e);
            throw new AppException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/receipts");
    }
}
