package org.motechproject.flash;

import javax.servlet.http.HttpServletRequest;

public class Flash {

    public static final String FLASH_IN_PREFIX = "flash.in.";
    public static final String FLASH_OUT_PREFIX = "flash.out.";

    public static boolean shouldBeConsumed(String attributeName) {
        return attributeName.startsWith(FLASH_OUT_PREFIX);
    }

    public static boolean shouldBeDestroyed(String attributeName) {
        return attributeName.startsWith(FLASH_IN_PREFIX);
    }

    private static String simpleAttributeName(String attributeName) {
        return attributeName.substring(FLASH_OUT_PREFIX.length());
    }

    public static boolean has(String attributeName, HttpServletRequest request) {
        return request.getAttribute(FLASH_IN_PREFIX + attributeName) != null;
    }

    public static void transfer(String attributeName, String attributeValue, HttpServletRequest request) {
        if (shouldBeConsumed(attributeName) && attributeValue != null) {
            request.setAttribute(FLASH_IN_PREFIX + simpleAttributeName(attributeName), attributeValue);
        }
    }

    public static String in(String attributeName, HttpServletRequest request) {
        Object attribute = request.getAttribute(FLASH_IN_PREFIX + attributeName);
        if (null != attribute)
            return attribute.toString();
        else
            return "";
    }

    public static void out(String attributeName, String value, HttpServletRequest request) {
        request.setAttribute(FLASH_OUT_PREFIX + attributeName, value);
    }
}
