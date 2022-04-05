package com.fenonq.myrestaurant.web.filter;

import com.fenonq.myrestaurant.db.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/receipts", "/users", "/account/dish-redactor", "/account/category-redactor",
        "/account/category-redactor/update-category", "/account/dish-redactor/update-dish"})
public class AdminFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getRoleId() != 1) {
            res.sendRedirect(req.getContextPath() + "/menu");
        } else {
            chain.doFilter(req, res);
        }
    }
}
