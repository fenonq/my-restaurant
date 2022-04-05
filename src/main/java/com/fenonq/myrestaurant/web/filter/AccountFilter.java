package com.fenonq.myrestaurant.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/account", "/account/change-password",
        "/account/dish-redactor", "/account/category-redactor"})
public class AccountFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/account/login");
        } else {
            chain.doFilter(req, res);
        }
    }
}
