package org.motechproject.export.utils;

import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;

public class AnnotationUtil {
    public static boolean hasAnnotation(Class clazz, Class annotationType) {
        return findAnnotation(clazz, annotationType) != null;
    }

    public static <A extends Annotation> A findAnnotation(Class clazz, Class<A> annotationType){
        return AnnotationUtils.findAnnotation(clazz, annotationType);
    }
}
