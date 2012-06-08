package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.diagnostics.repository.AllDiagnosticMethods;
import org.motechproject.diagnostics.response.DiagnosticsResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticsControllerTest {

    @Mock
    private AllDiagnosticMethods allDiagnosticMethods;

    private DiagnosticsController diagnosticsController;

    @Before
    public void setUp() {
        initMocks(this);
        diagnosticsController = new DiagnosticsController(allDiagnosticMethods);
    }

    @Test
    public void shouldInvokeAllTheDiagnosticMethods() throws InvocationTargetException, IllegalAccessException {
        List<DiagnosticsResponse> expectedDiagnosticsResponses = new ArrayList<DiagnosticsResponse>();
        when(allDiagnosticMethods.runAllDiagnosticMethods()).thenReturn(expectedDiagnosticsResponses);

        List<DiagnosticsResponse> actualDiagnosticsResponses = diagnosticsController.get();

        assertEquals(expectedDiagnosticsResponses, actualDiagnosticsResponses);
    }
}
