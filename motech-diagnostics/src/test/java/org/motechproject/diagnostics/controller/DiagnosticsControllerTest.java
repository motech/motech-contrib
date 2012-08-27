package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.diagnostics.DiagnosticResponseBuilder;
import org.motechproject.diagnostics.service.DiagnosticsService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        when(allDiagnosticMessageBuilder.createResponseMessage(any(List.class))).thenReturn("Diagnostic Response");

        diagnosticsController.runDiagnostics("name", httpResponse);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(servletOutputStream).print(captor.capture());
        assertEquals("Diagnostic Response", captor.getValue().trim());
    }
}
