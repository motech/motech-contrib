package org.motechproject.diagnostics.util;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.stereotype.Component;

@Component
public class TestClass {

    public static int methodExecutionCount = 0;
    public static boolean methodWithAnnotationRun = false;

    @Diagnostic(name = "testDiagnostics1")
    public DiagnosticsResult method1WithAnnotation() {
        methodExecutionCount++;
        methodWithAnnotationRun = true;
        return new DiagnosticsResult(true, "test message");
    }

    @Diagnostic(name = "testDiagnostics2")
    public DiagnosticsResult method2WithAnnotation() {
        methodExecutionCount++;
        return new DiagnosticsResult(false, "test message");
    }

    public void methodWithoutAnnotation() {
        methodExecutionCount++;
    }
}