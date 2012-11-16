package org.motechproject.contrib.common;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static junit.framework.Assert.assertEquals;

public class ReflectionUtilTest {

    @Test
    public void shouldInvokeAnnotatedMethod(){
        Dummy dummy = new Dummy();
        String expected = dummy.getFoo("arwen");
        Object output = ReflectionUtil.invokeAnnotatedMethod(dummy, DummyEntity.class,"arwen");
        assertEquals(expected,(String)output);
    }
}


class Dummy{

    @DummyEntity
    public String getFoo(String param){
        return param+"aragorn";
    }


}

@Retention(RUNTIME)
@Target({ElementType.METHOD})
 @interface DummyEntity {
}
