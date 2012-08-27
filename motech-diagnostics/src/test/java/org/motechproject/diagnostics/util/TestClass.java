package org.motechproject.diagnostics.util;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.springframework.stereotype.Component;

@Component
public class TestClass {

    public static int methodExecutionCount = 0;
    public static boolean methodWithAnnotationRun = false;

    @Diagnostic(name = "pass diagnostics 1")
    public DiagnosticsResult passDiagnostics1() {
        methodExecutionCount++;
        return new DiagnosticsResult(DiagnosticsStatus.PASS, "pass message 1");
    }

    @Diagnostic(name = "pass diagnostics 2")
    public DiagnosticsResult passDiagnostics2() {
        methodExecutionCount++;
        return new DiagnosticsResult(DiagnosticsStatus.PASS, "pass message 2");
    }

    @Diagnostic(name = "fail diagnostics")
    public DiagnosticsResult failDiagnostics() {
        methodExecutionCount++;
        return new DiagnosticsResult(DiagnosticsStatus.FAIL, "fail message");
    }

    @Diagnostic(name = "null diagnostics")
    public DiagnosticsResult nullDiagnostics() {
        methodExecutionCount++;
        return null;
    }

    @Diagnostic(name = "warn diagnostics")
    public DiagnosticsResult badDiagnostics() {
        methodExecutionCount++;
        return new DiagnosticsResult(DiagnosticsStatus.WARN, "warn message");
    }

    @Diagnostic(name = "unknown diagnostics")
    public DiagnosticsResult unknownDiagnostics() {
        methodExecutionCount++;
        return new DiagnosticsResult(DiagnosticsStatus.UNKNOWN, "unknown message");
    }

    @Diagnostic(name = "exception diagnostics")
    public void exceptionDiagnostics() {
        methodExecutionCount++;
        throw new RuntimeException("some exception");
    }

    public void methodWithoutAnnotation() {
        methodExecutionCount++;
    }
}
