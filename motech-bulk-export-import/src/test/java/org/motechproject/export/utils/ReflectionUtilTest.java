package org.motechproject.export.utils;

import org.junit.Test;
import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReflectionUtilTest {

    @Test
    public void shouldVerifyIfObjectHasAGivenAnnotation() {
        assertTrue(ReflectionUtil.hasAnnotation(ClassWithAnnotation.class, ExcelDataSource.class));
        assertFalse(ReflectionUtil.hasAnnotation(ClassWithNoAnnotation.class, ExcelDataSource.class));

        //verify proxy classes too
        assertTrue(ReflectionUtil.hasAnnotation(mock(ClassWithAnnotation.class).getClass(), ExcelDataSource.class));
    }

    @Test
    public void shouldVerifyIfMethodHasAGivenAnnotation() throws NoSuchMethodException {
        Method methodWithoutAnnotation = ClassWithNoAnnotation.class.getDeclaredMethod("method", null);
        Method methodWithAnnotation = ClassWithAnnotation.class.getDeclaredMethod("method", null);
        Method methodWithAnnotationFromProxyClass = mock(ClassWithAnnotation.class).getClass().getDeclaredMethod("method", null);

        assertFalse(ReflectionUtil.hasAnnotation(methodWithoutAnnotation, DataProvider.class));
        assertTrue(ReflectionUtil.hasAnnotation(methodWithAnnotation, DataProvider.class));
        assertTrue(ReflectionUtil.hasAnnotation(methodWithAnnotationFromProxyClass, DataProvider.class));
    }
}

class ClassWithNoAnnotation{

    public void method(){

    }

}


@ExcelDataSource(name = "name")
class ClassWithAnnotation {

    @DataProvider
    public List<ClassWithNoAnnotation> method(){
        return null;
    }
}