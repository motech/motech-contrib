package org.motechproject.diagnostics.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticsServiceTest {

    DiagnosticsService diagnosticsService;

    @Mock
    DiagnosticConfiguration diagnosticProperties;

    @Before
    public void setUp() {
         initMocks(this);
    }

    @Test
    public void givenADiagnosticRunsItByName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        when(diagnosticProperties.diagnosticServices()).thenReturn("testDiagnostic");
        diagnosticsService = new DiagnosticsService(asList((Diagnostics) diagnostic), diagnosticProperties);

        diagnosticsService.run("testDiagnostic");
        assertTrue(diagnostic.isCalled());
    }

    @Test
    public void givenASetOfDiagnosticsRunsADiagnosticByName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        AnotherTestDiagnostic anotherDiagnostic = new AnotherTestDiagnostic();
        when(diagnosticProperties.diagnosticServices()).thenReturn("anotherTestDiagnostic,testDiagnostic");
        diagnosticsService = new DiagnosticsService(asList(diagnostic, anotherDiagnostic), diagnosticProperties);

        diagnosticsService.run("anotherTestDiagnostic");
        assertFalse(diagnostic.isCalled());
        assertTrue(anotherDiagnostic.isCalled());
    }

    @Test(expected = RuntimeException.class)
    public void givenADiagnosticThrowsAnExceptionWhenNameDoesNotMatchDiagnosticName() {
        TestDiagnostic diagnostic = new TestDiagnostic();
        AnotherTestDiagnostic anotherDiagnostic = new AnotherTestDiagnostic();
        when(diagnosticProperties.diagnosticServices()).thenReturn("anotherTestDiagnostic,testDiagnostic");
        diagnosticsService = new DiagnosticsService(asList(diagnostic, anotherDiagnostic), diagnosticProperties);

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



