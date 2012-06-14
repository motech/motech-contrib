package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.diagnostics.repository.AllDiagnosticMethods;
import org.motechproject.diagnostics.response.DiagnosticsResponse;
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
    private DiagnosticsController diagnosticsController;

    @Before
    public void setUp() {
        initMocks(this);
        diagnosticsController = new DiagnosticsController(allDiagnosticMethods);
    }

    @Test
    public void shouldInvokeAllTheDiagnosticMethods() throws InvocationTargetException, IllegalAccessException, IOException {
        List<DiagnosticsResponse> expectedDiagnosticsResponses = new ArrayList<DiagnosticsResponse>();
        expectedDiagnosticsResponses.add(new DiagnosticsResponse("Test diagnostic", new DiagnosticsResult(true, "Test diagnostic run")));

        when(allDiagnosticMethods.runAllDiagnosticMethods()).thenReturn(expectedDiagnosticsResponses);
        when(httpResponse.getOutputStream()).thenReturn(servletOutputStream);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        diagnosticsController.getDiagnostics(httpResponse);

        verify(servletOutputStream).print(captor.capture());
        String responseValue = captor.getValue();

        assertEquals("Name : Test diagnostic\nStatus : true\nTest diagnostic run", responseValue.trim());
    }
}
