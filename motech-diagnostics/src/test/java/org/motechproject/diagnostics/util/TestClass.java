package org.motechproject.diagnostics.util;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.springframework.stereotype.Component;

@Component
public class TestClass implements Diagnostics {

    public static int methodExecutionCount = 0;
    public static boolean methodWithAnnotationRun = false;

    @Diagnostic(name = "testDiagnostics1")
    public DiagnosticsResult method1WithAnnotation() {
        methodExecutionCount++;
        methodWithAnnotationRun = true;
        return new DiagnosticsResult("test message 1", "true", Status.Success);
    }

    @Diagnostic(name = "testDiagnostics2")
    public DiagnosticsResult method2WithAnnotation() {
        methodExecutionCount++;
        return new DiagnosticsResult("test message 2", "false", Status.Success);
    }

    @Diagnostic(name = "testDiagnosticsWithNullResult")
    public DiagnosticsResult method3WithNullResult() {
        methodExecutionCount++;
        return null;
    }

    public void methodWithoutAnnotation() {
        methodExecutionCount++;
    }

    @Override
    public String name() {
        return "testDiagnostics";
    }

    @Override
    public boolean canPerformDiagnostics() {
        return true;
    }
}
