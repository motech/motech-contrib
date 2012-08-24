package org.motechproject.diagnostics.util;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.stereotype.Component;

@Component
public class TestClass {

    public static int methodExecutionCount = 0;
    public static boolean methodWithAnnotationRun = false;

    @Diagnostic(name = "testDiagnostics1")
    public DiagnosticsResult<Boolean> method1WithAnnotation() {
        methodExecutionCount++;
        methodWithAnnotationRun = true;
        return new DiagnosticsResult<Boolean>("test message 1", true);
    }

    @Diagnostic(name = "testDiagnostics2")
    public DiagnosticsResult<Boolean> method2WithAnnotation() {
        methodExecutionCount++;
        return new DiagnosticsResult<Boolean>("test message 2", false);
    }

    @Diagnostic(name = "testDiagnosticsWithNullResult")
    public DiagnosticsResult method3WithNullResult() {
        methodExecutionCount++;
        return null;
    }

    public void methodWithoutAnnotation() {
        methodExecutionCount++;
    }
}
