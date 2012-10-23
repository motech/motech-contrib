package org.motechproject.diagnostics.controller;

import org.apache.commons.io.IOUtils;
import org.motechproject.diagnostics.response.Status;
import org.motechproject.diagnostics.velocity.builder.DiagnosticResponseBuilder;
import org.motechproject.diagnostics.response.ExceptionResponse;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnostics.service.DiagnosticsService;
import org.motechproject.diagnostics.velocity.builder.LogFilesResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.motechproject.diagnostics.response.Status.Success;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/diagnostics/")
public class DiagnosticsController {

    DiagnosticsService diagnosticsService;
    private DiagnosticResponseBuilder diagnosticResponseBuilder;
    private LogFilesResponseBuilder logFilesResponseBuilder;

    @Autowired
    public DiagnosticsController(DiagnosticsService diagnosticsService, DiagnosticResponseBuilder diagnosticResponseBuilder, LogFilesResponseBuilder logFilesResponseBuilder) {
        this.diagnosticsService = diagnosticsService;
        this.diagnosticResponseBuilder = diagnosticResponseBuilder;
        this.logFilesResponseBuilder = logFilesResponseBuilder;
    }

    @RequestMapping(value = "service/{serviceName}", method = GET)
    @ResponseBody
    public ServiceResult runDiagnostics(@PathVariable("serviceName") String name, HttpServletResponse response) throws IOException {
        ServiceResult serviceResult = diagnosticsService.run(name);
        if(serviceResult.status() != Success){
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @RequestMapping(value = "show/all/json", method = GET)
    @ResponseBody
    public List<ServiceResult> getDiagnostics() throws InvocationTargetException, IllegalAccessException, IOException {
        List<ServiceResult> serviceResults = diagnosticsService.runAll();
        return serviceResults;
    }

    @RequestMapping(value = "show/all", method = GET)
    public void viewDiagnostics(HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        String diagnosticsResponse = diagnosticResponseBuilder.createResponseMessage(getDiagnostics());
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
