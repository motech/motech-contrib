package org.motechproject.diagnostics.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
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
        assertTrue(isValidDiagnosticMethod(TestClass.class.getMethod("passDiagnostics1")));
        assertFalse(isValidDiagnosticMethod(TestClass.class.getMethod("methodWithoutAnnotation")));
    }

    @Test
    public void shouldInvokeAllDiagnosticsMethods() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestClass testClass = new TestClass();
        String diagnosticsName = "pass diagnostics 1";
        DiagnosticMethod diagnosticMethod = new DiagnosticMethod(diagnosticsName, testClass, testClass.getClass().getMethod("passDiagnostics1"));

        DiagnosticsResponse diagnosticsResponse = diagnosticMethod.run();

        assertEquals(diagnosticsName, diagnosticsResponse.getName());
        assertEquals(DiagnosticsStatus.PASS, diagnosticsResponse.getResult().getStatus());
        assertEquals("pass message 1", diagnosticsResponse.getResult().getMessage());
    }

    @Test
    public void shouldReturnResponseAsUnknownIfResultIsNull() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestClass testClass = new TestClass();
        String diagnosticsName = "null diagnostics";
        DiagnosticMethod diagnosticMethod = new DiagnosticMethod(diagnosticsName, testClass, testClass.getClass().getMethod("nullDiagnostics"));

        DiagnosticsResponse diagnosticsResponse = diagnosticMethod.run();

        assertEquals(DiagnosticsStatus.UNKNOWN, diagnosticsResponse.getResult().getStatus());
        assertEquals("Null Result", diagnosticsResponse.getResult().getMessage());
    }

    @Test
    public void shouldCatchExceptionAndReturnFailedResult() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestClass testClass = new TestClass();
        String diagnosticsName = "exceptiob diagnostics";
        DiagnosticMethod diagnosticMethod = new DiagnosticMethod(diagnosticsName, testClass, testClass.getClass().getMethod("exceptionDiagnostics"));

        DiagnosticsResponse diagnosticsResponse = diagnosticMethod.run();

        assertEquals(DiagnosticsStatus.FAIL, diagnosticsResponse.getResult().getStatus());
    }
}
