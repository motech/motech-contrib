package org.motechproject.diagnostics.service;

import org.junit.Test;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiagnosticsServiceTest {

    DiagnosticsService diagnosticsService;

    @Test
    public void givenADiagnosticRunsItByName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        diagnosticsService = new DiagnosticsService(asList((Diagnostics) diagnostic), "testDiagnostic");

        diagnosticsService.run("testDiagnostic");
        assertTrue(diagnostic.isCalled());
    }

    @Test
    public void givenASetOfDiagnosticsRunsADiagnosticByName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        AnotherTestDiagnostic anotherDiagnostic = new AnotherTestDiagnostic();
        diagnosticsService = new DiagnosticsService(asList(diagnostic, anotherDiagnostic), "anotherTestDiagnostic,testDiagnostic");

        diagnosticsService.run("anotherTestDiagnostic");
        assertFalse(diagnostic.isCalled());
        assertTrue(anotherDiagnostic.isCalled());
    }

    @Test(expected = RuntimeException.class)
    public void givenADiagnosticThrowsAnExceptionWhenNameDoesNotMatchDiagnosticName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        AnotherTestDiagnostic anotherDiagnostic = new AnotherTestDiagnostic();
        diagnosticsService = new DiagnosticsService(asList(diagnostic, anotherDiagnostic), "anotherTestDiagnostic,testDiagnostic");

        diagnosticsService.run("randomName");
    }

    public static class TestDiagnostic implements Diagnostics {

        private boolean isCalled;

        @Override
        public String name() {
            return "testDiagnostic";
        }

        @Override
        public boolean canPerformDiagnostics() {
            return true;
        }

        @Diagnostic(name = "diagnosticMethod")
        public DiagnosticsResult run() {
            isCalled = true;
            return null;
        }

        public boolean isCalled() {
            return isCalled;
        }
    }

    public static class AnotherTestDiagnostic implements Diagnostics {

        private boolean isCalled;

        @Override
        public String name() {
            return "anotherTestDiagnostic";
        }

        @Override
        public boolean canPerformDiagnostics() {
            return true;
        }

        @Diagnostic(name = "diagnosticMethod")
        public DiagnosticsResult run() {
            isCalled = true;
            return null;
        }

        public boolean isCalled() {
            return isCalled;
        }
    }
}



