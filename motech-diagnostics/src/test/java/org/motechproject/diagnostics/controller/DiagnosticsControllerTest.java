package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.diagnostics.DiagnosticResponseBuilder;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.motechproject.diagnostics.repository.AllDiagnosticMethods;
import org.motechproject.diagnostics.service.DiagnosticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticsControllerTest {

    @Mock
    private DiagnosticsService diagnosticsService;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    ServletOutputStream servletOutputStream;

    @Mock
    private DiagnosticResponseBuilder allDiagnosticMessageBuilder;
    private DiagnosticsController diagnosticsController;

    @Before
    public void setUp() throws IOException {
        initMocks(this);
        when(httpResponse.getOutputStream()).thenReturn(servletOutputStream);
        diagnosticsController = new DiagnosticsController(diagnosticsService, allDiagnosticMessageBuilder);
    }

    @Test
    public void shouldInvokeDiagnosticsByName() throws IOException {
        diagnosticsController.runDiagnostics("name", httpResponse);
        verify(diagnosticsService).run("name");
    }

    @Test
    public void shouldPrintDiagnosticMessageResponse() throws IOException {
        List<DiagnosticsResult> expectedDiagnosticsResponses = Arrays.asList(new DiagnosticsResult("Test diagnostic", new DiagnosticsResult(DiagnosticsStatus.PASS.name(), "Test diagnostic run")));
        when(diagnosticsService.run("name")).thenReturn(expectedDiagnosticsResponses);

        List<DiagnosticsResult> diagnosticsResults = diagnosticsController.runDiagnostics("name", httpResponse);

        assertEquals(expectedDiagnosticsResponses, diagnosticsResults);
    }

    @Test
    public void shouldInvokeAllTheDiagnosticMethods() throws InvocationTargetException, IllegalAccessException, IOException {
        List<DiagnosticsResult> expectedDiagnosticsResponses = Arrays.asList(new DiagnosticsResult("Test diagnostic", new DiagnosticsResult(DiagnosticsStatus.PASS.name(), "Test diagnostic run")));
        when(diagnosticsService.runAll()).thenReturn(expectedDiagnosticsResponses);
        when(httpResponse.getOutputStream()).thenReturn(servletOutputStream);
        when(allDiagnosticMessageBuilder.createResponseMessage(expectedDiagnosticsResponses)).thenReturn("Diagnostic Response");
        List<DiagnosticsResult> diagnostics = diagnosticsController.getDiagnostics(httpResponse);
        assertEquals(expectedDiagnosticsResponses, diagnostics);
    }
}
