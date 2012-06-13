package org.motechproject.diagnostics.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.util.TestClass;

import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.*;
import static org.motechproject.diagnostics.model.DiagnosticMethod.isValidDiagnosticMethod;

public class DiagnosticMethodTest {
    @Before
    @After
    public void setUpAndTearDown() {
        TestClass.methodExecutionCount = 0;
    }

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
        assertEquals("test message 1", diagnosticsResponse.getResult().getMessage());
    }

    @Test
    public void shouldReturnResponseAsNullIfResultIsNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestClass testClass = new TestClass();
        String methodName = "testMethod3";
        DiagnosticMethod diagnosticMethod = new DiagnosticMethod(methodName, testClass, testClass.getClass().getMethod("method3WithNullResult"));

        DiagnosticsResponse diagnosticsResponse = diagnosticMethod.run();

        assertNull(diagnosticsResponse);
    }
}
