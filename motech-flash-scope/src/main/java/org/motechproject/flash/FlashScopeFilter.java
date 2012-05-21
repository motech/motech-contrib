package org.motechproject.flash;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.motechproject.flash.FlashAttributeName.*;

@Component
public class FlashScopeFilter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setFlashParamsInRequestAttributesAndRemoveThemFromCookie(request);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        storeFlashParamsInCookie(request, response);
        super.postHandle(request, response, handler, modelAndView);
    }

    private void setFlashParamsInRequestAttributesAndRemoveThemFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                setFlashParamsInRequestAttributes(request, cookie);
                cookie.setMaxAge(0);
            }
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
                Cookie cookie = new Cookie(attributeName, (String) request.getAttribute(attributeName));
                cookie.setMaxAge(-1);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        removeFlashAttributes(request);
    }

    private void removeFlashAttributes(HttpServletRequest request) {
        Enumeration attributeNames = request.getAttributeNames();
        List<String> toBeRemoved = new ArrayList<String>();
        while (attributeNames.hasMoreElements()) {
            String attributeName = (String) attributeNames.nextElement();
            if (shouldBeConsumed(attributeName) || shouldBeDestroyed(attributeName)) {
                toBeRemoved.add(attributeName);
            }
        }
        for (String attribute : toBeRemoved) {
            request.removeAttribute(attribute);
        }
    }
}