package org.motechproject.export.utils;

import org.junit.Test;
import org.motechproject.export.annotation.ExcelDataSource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AnnotationUtilTest {

    @Test
    public void shouldVerifyIfObjectHasAGivenAnnotation() {
        assertTrue(AnnotationUtil.hasAnnotation(ClassWithAnnotation.class, ExcelDataSource.class));
        assertFalse(AnnotationUtil.hasAnnotation(ClassWithNoAnnotation.class, ExcelDataSource.class));

        //verify proxy classes too
        assertTrue(AnnotationUtil.hasAnnotation(mock(ClassWithAnnotation.class).getClass(), ExcelDataSource.class));
    }
}

class ClassWithNoAnnotation{

}


@ExcelDataSource(name = "name")
class ClassWithAnnotation {

}