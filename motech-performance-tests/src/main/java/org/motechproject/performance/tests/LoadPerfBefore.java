package org.motechproject.performance.tests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadPerfBefore {
    /*Higher the value, lower the priority*/
    int priority();
    int concurrentUsers();
}
