package org.motechproject.contrib.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Method getAnnotatedMethod(Object o, Class annotationClass) {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }
        return null;
    }

    public static Object invokeMethod(Method method, Object o, Object... args) {
        try {
            return method.invoke(o,args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error executing method "+method.getName()+" on "+o.getClass().getSimpleName()+"\n"+e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error executing method "+method.getName()+" on "+o.getClass().getSimpleName()+"\n"+e.getMessage());
        }catch (IllegalArgumentException e) {
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        } catch (ClassCastException e) {
            throw new RuntimeException("Format of data method is invalid." + e.getMessage());
        }
    }

    public static Object invokeAnnotatedMethod( Object o,Class annotation,Object... args) {
        Method annotatedMethod = getAnnotatedMethod(o, annotation);
        return annotatedMethod != null ? invokeMethod(annotatedMethod, o,args) : null;
    }
}
