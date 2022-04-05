package com.fenonq.myrestaurant.web.tag;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class PriceCustomTag extends TagSupport {
    private static final Logger log = LogManager.getLogger(PriceCustomTag.class.getName());
    private static final String HRYVNIA_SIGN = "&#8372";

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.write(HRYVNIA_SIGN);
        } catch (IOException e) {
            log.error("Error in PriceCustomTag", e);
        }
        return SKIP_BODY;
    }
}
