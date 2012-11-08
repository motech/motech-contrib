package org.motechproject.diagnosticsweb.controller;


import org.apache.commons.io.IOUtils;
import org.motechproject.diagnostics.response.ExceptionResponse;
import org.motechproject.diagnostics.service.DiagnosticsService;
import org.motechproject.diagnosticsweb.velocity.builder.DiagnosticResponseBuilder;
import org.motechproject.diagnosticsweb.velocity.builder.LogFilesResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/diagnostics/")
public class DiagnosticsWebController {

    private LogFilesResponseBuilder logFilesResponseBuilder;
    DiagnosticsService diagnosticsService;
    private DiagnosticResponseBuilder diagnosticResponseBuilder;

    @Autowired
    public DiagnosticsWebController(LogFilesResponseBuilder logFilesResponseBuilder, DiagnosticsService diagnosticsService, DiagnosticResponseBuilder diagnosticResponseBuilder) {
        this.logFilesResponseBuilder = logFilesResponseBuilder;
        this.diagnosticsService = diagnosticsService;
        this.diagnosticResponseBuilder = diagnosticResponseBuilder;
    }

    @RequestMapping(value = "show/all", method = GET)
    public void viewDiagnostics(HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        String diagnosticsResponse = diagnosticResponseBuilder.createResponseMessage(diagnosticsService.runAll());
        response.getOutputStream().print(diagnosticsResponse);
    }


    @RequestMapping(method = RequestMethod.GET, value = "show/logs")
    public void showLogs(HttpServletResponse response) throws Exception {
        String logFilesResponse = logFilesResponseBuilder.createResponse();
        response.getOutputStream().print(logFilesResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "show/logs/{file_name:.*}")
    public void getLog(@PathVariable("file_name") String logFilename, HttpServletResponse response) throws Exception {
        IOUtils.copy(logFilesResponseBuilder.getLogFile(logFilename), response.getOutputStream());
        response.setContentType("text/plain");
        response.flushBuffer();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(final Exception exception, HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(SC_INTERNAL_SERVER_ERROR);

        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return new ExceptionResponse(exception.getMessage(), stringWriter.toString()).toString();
    }
}
