package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.diagnostics.DiagnosticResponseBuilder;
import org.motechproject.diagnostics.repository.AllDiagnosticMethods;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticsControllerTest {

    @Mock
    private AllDiagnosticMethods allDiagnosticMethods;
    @Mock
    private HttpServletResponse httpResponse;
    @Mock
    ServletOutputStream servletOutputStream;
    @Mock
    private DiagnosticResponseBuilder allDiagnosticMessageBuilder;

    private DiagnosticsController diagnosticsController;

    @Before
    public void setUp() {
        initMocks(this);
        diagnosticsController = new DiagnosticsController(allDiagnosticMethods,allDiagnosticMessageBuilder);
    }

    @Test
    public void shouldInvokeAllTheDiagnosticMethods() throws InvocationTargetException, IllegalAccessException, IOException {
        List<DiagnosticsResult> expectedDiagnosticsResponses = new ArrayList<DiagnosticsResult>();
        expectedDiagnosticsResponses.add(new DiagnosticsResult("Test diagnostic run", true));

        when(allDiagnosticMethods.runAllDiagnosticMethods()).thenReturn(expectedDiagnosticsResponses);
        when(httpResponse.getOutputStream()).thenReturn(servletOutputStream);
        when(allDiagnosticMessageBuilder.createResponseMessage(expectedDiagnosticsResponses)).thenReturn("Diagnostic Response");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        diagnosticsController.getDiagnostics(httpResponse);

        verify(servletOutputStream).print(captor.capture());
        String responseValue = captor.getValue();

        assertEquals("Diagnostic Response", responseValue.trim());
    }
}
