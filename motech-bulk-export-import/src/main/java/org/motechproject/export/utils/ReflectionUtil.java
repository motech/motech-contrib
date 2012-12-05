package org.motechproject.export.utils;

import org.springframework.aop.TargetClassAware;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ReflectionUtil {
    public static boolean hasAnnotation(Class clazz, Class annotationType) {
        return findAnnotation(clazz, annotationType) != null;
    }

    public static <A extends Annotation> A findAnnotation(Class clazz, Class<A> annotationType){
        return AnnotationUtils.findAnnotation(clazz, annotationType);
    }

    public static boolean hasAnnotation(Method method, Class annotationType){
        return findAnnotation(method, annotationType) != null;
    }

    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType){
        return AnnotationUtils.findAnnotation(method, annotationType);
    }

    public static Type genericReturnType(Object object, String methodName) {
        Class clazz;
        if(object instanceof TargetClassAware){  //spring proxy class
            clazz = AopUtils.getTargetClass(object);
        } else {
            clazz = object.getClass();
        }

        for(Method method : clazz.getMethods()){
            if(method.getName().equals(methodName)){
               return method.getGenericReturnType();
            }
        }

        return null;
    }
}
