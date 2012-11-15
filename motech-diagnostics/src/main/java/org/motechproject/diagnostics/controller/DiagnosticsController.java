package org.motechproject.diagnostics.controller;

import org.motechproject.diagnostics.response.ExceptionResponse;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnostics.service.DiagnosticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    public DiagnosticsController(DiagnosticsService diagnosticsService) {
        this.diagnosticsService = diagnosticsService;
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

    @RequestMapping(value = "service/_all", method = GET)
    @ResponseBody
    public List<ServiceResult> getDiagnostics() throws InvocationTargetException, IllegalAccessException, IOException {
        List<ServiceResult> serviceResults = diagnosticsService.runAll();
        return serviceResults;
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
