package org.motechproject.web.context;

public class HttpThreadContext {

    private static final ThreadLocal<String> httpThreadLocal = new ThreadLocal();

    public static void set(String contextVar) {
        httpThreadLocal.set(contextVar);
    }

    public static void unset() {
        httpThreadLocal.remove();
    }

    public static String get() {
        return httpThreadLocal.get();
    }
}
