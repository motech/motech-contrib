package org.motechproject.caselogs.controller;


import org.motechproject.caselogs.velocity.builder.CaseLogsResponseBuilder;
import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.service.CaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/caselogs/")
public class CaseLogsController {

    private final CaseLogService caseLogService;
    private final CaseLogsResponseBuilder caseLogsResponseBuilder;

    @Autowired
    public CaseLogsController(CaseLogService caseLogService, CaseLogsResponseBuilder caseLogsResponseBuilder) {
        this.caseLogService = caseLogService;
        this.caseLogsResponseBuilder = caseLogsResponseBuilder;
    }

    @RequestMapping(value = "all", method = GET)
    public void showAllLogs(HttpServletResponse response) throws IOException {
        List<CaseLog> allCaseLogs = caseLogService.getAll();
        String caseLogsResponse = caseLogsResponseBuilder.createResponseMessage(allCaseLogs);
        response.getOutputStream().print(caseLogsResponse);
    }

    @RequestMapping(value = "filter", method = POST)
    public void filterLogs(@RequestParam("entityId") String entityId, @RequestParam("requestType") String requestType, HttpServletResponse response) throws IOException {
        List<CaseLog> allCaseLogs = caseLogService.filter(entityId, requestType);
        String caseLogsResponse = caseLogsResponseBuilder.createResponseMessage(allCaseLogs);
        response.getOutputStream().print(caseLogsResponse);
    }
}
