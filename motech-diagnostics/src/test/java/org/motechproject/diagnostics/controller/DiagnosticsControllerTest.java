package org.motechproject.diagnostics.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.DiagnosticsStatus;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnostics.response.Status;
import org.motechproject.diagnostics.service.DiagnosticsService;
import org.motechproject.diagnostics.velocity.builder.DiagnosticResponseBuilder;
import org.motechproject.diagnostics.velocity.builder.LogFilesResponseBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticsControllerTest {

    @Mock
    private DiagnosticsService diagnosticsService;
    @Mock
    private LogFilesResponseBuilder logFilesResponseBuilder;
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
        diagnosticsController = new DiagnosticsController(diagnosticsService, allDiagnosticMessageBuilder,logFilesResponseBuilder);
    }

    @Test
    public void shouldInvokeDiagnosticsByName() throws IOException {
        diagnosticsController.runDiagnostics("name");
        verify(diagnosticsService).run("name");
    }

    @Test
    public void shouldPrintDiagnosticMessageResponse() throws IOException {
        String serviceName = "name";
        List<DiagnosticsResult> expectedDiagnosticsResponses = asList(new DiagnosticsResult("Test diagnostic", Arrays.asList(new DiagnosticsResult(DiagnosticsStatus.PASS.name(), "Test diagnostic run", Status.Success))));
        ServiceResult expectedServiceResult = new ServiceResult(serviceName, expectedDiagnosticsResponses);
        when(diagnosticsService.run(serviceName)).thenReturn(expectedServiceResult);

        ServiceResult serviceResult = diagnosticsController.runDiagnostics(serviceName);

        assertEquals(expectedServiceResult, serviceResult);
    }

    @Test
    public void shouldInvokeAllTheDiagnosticMethods() throws InvocationTargetException, IllegalAccessException, IOException {
        List<DiagnosticsResult> expectedDiagnosticsResponses = asList(new DiagnosticsResult("Test diagnostic", Arrays.asList(new DiagnosticsResult(DiagnosticsStatus.PASS.name(), "Test diagnostic run", Status.Success))));
        List<ServiceResult> expectedServiceResults = asList(new ServiceResult("serviceName", expectedDiagnosticsResponses));
        when(diagnosticsService.runAll()).thenReturn(expectedServiceResults);
        when(httpResponse.getOutputStream()).thenReturn(servletOutputStream);
        when(allDiagnosticMessageBuilder.createResponseMessage(expectedServiceResults)).thenReturn("Diagnostic Response");

        List<ServiceResult> actualServiceResults = diagnosticsController.getDiagnostics();
        assertEquals(expectedServiceResults, actualServiceResults);
    }
}
