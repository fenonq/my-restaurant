package com.fenonq.myrestaurant.web.filter;

import com.fenonq.myrestaurant.db.entity.enums.Locales;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LanguageFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        req.getSession().setAttribute("localesArr", Locales.values());
        String locale = req.getParameter("locale");

        if (locale != null) {
            if(Locales.contains(locale)) {
                req.getSession().setAttribute("locale", locale);
            }
        } else if (req.getSession().getAttribute("locale") == null) {
            req.getSession().setAttribute("locale", Locales.EN.toString());
        }
        chain.doFilter(request, response);
    }

}
