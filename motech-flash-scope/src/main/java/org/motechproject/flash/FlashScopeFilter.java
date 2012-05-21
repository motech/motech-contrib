package org.motechproject.flash;


import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.motechproject.flash.FlashAttributeName.*;

public class FlashScopeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            setFlashParamsInRequestAttributesAndRemoveThemFromCookie((HttpServletRequest) servletRequest);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        if (servletRequest instanceof HttpServletRequest) {
            storeFlashParamsInCookie((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        }
    }

    @Override
    public void destroy() {
    }

    private void setFlashParamsInRequestAttributesAndRemoveThemFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            setFlashParamsInRequestAttributes(request, cookie);
            cookie.setMaxAge(0);
        }
    }

    private void setFlashParamsInRequestAttributes(HttpServletRequest request, Cookie cookie) {
        if (shouldBeConsumed(cookie.getName()) && cookie.getValue() != null) {
            request.setAttribute(in(simpleAttributeName(cookie.getName())), cookie.getValue());
        }
    }

    private void storeFlashParamsInCookie(HttpServletRequest request, HttpServletResponse response) {
        Enumeration attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = (String) attributeNames.nextElement();
            if (shouldBeConsumed(attributeName)) {
                response.addCookie(new Cookie(attributeName, request.getAttribute(attributeName).toString()));
                request.removeAttribute(attributeName);
            } else if (shouldBeDestroyed(attributeName)) {
                request.removeAttribute(attributeName);
            }
        }

    }
}