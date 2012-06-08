package org.motechproject.diagnostics.model;

import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.util.TestClass;

import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.*;
import static org.motechproject.diagnostics.model.DiagnosticMethod.isValidDiagnosticMethod;

public class DiagnosticMethodTest {

    @Test
    public void shouldSayIfAMethodIsADiagnosticMethod() throws NoSuchMethodException {
        assertTrue(isValidDiagnosticMethod(TestClass.class.getMethod("method1WithAnnotation")));
        assertFalse(isValidDiagnosticMethod(TestClass.class.getMethod("methodWithoutAnnotation")));
    }

    @Test
    public void shouldInvokeAllDiagnosticsMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestClass testClass = new TestClass();
        String methodName = "testMethod1";
        DiagnosticMethod diagnosticMethod = new DiagnosticMethod(methodName, testClass, testClass.getClass().getMethod("method1WithAnnotation"));

        DiagnosticsResponse diagnosticsResponse = diagnosticMethod.run();

        assertTrue(testClass.methodWithAnnotationRun);
        assertEquals(methodName, diagnosticsResponse.getName());
        assertTrue(diagnosticsResponse.getResult().getStatus());
        assertEquals("test message", diagnosticsResponse.getResult().getMessage());
    }
}
