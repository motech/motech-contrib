package org.motechproject.flash;

public class FlashAttributeName {

    public static final String FLASH_IN_PREFIX = "flash.in.";
    public static final String FLASH_OUT_PREFIX = "flash.out.";

    public static boolean shouldBeConsumed(String attributeName) {
        return attributeName.startsWith(FLASH_OUT_PREFIX);
    }

    public static boolean shouldBeDestroyed(String attributeName) {
        return attributeName.startsWith(FLASH_IN_PREFIX);
    }

    public static String simpleAttributeName(String attributeName) {
        // Remove the flash out prefix
        return attributeName.substring(10);
    }

    public static String in(String attributeName) {
        return FLASH_IN_PREFIX + attributeName;
    }

    public static String out(String attributeName) {
        return FLASH_OUT_PREFIX + attributeName;
    }
}
