package org.motechproject.caselogs.controller;


import org.motechproject.caselogs.configuration.CaseLogConfiguration;
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

    public static final String CONTENT_PATH = "case-log-browser/views/content/body.vm";
    public static final String VIEW_PATH = "case-log-browser/views/content/logs.vm";

    private CaseLogService caseLogService;
    private CaseLogsResponseBuilder caseLogsResponseBuilder;
    private CaseLogConfiguration configuration;

    @Autowired
    public CaseLogsController(CaseLogService caseLogService, CaseLogsResponseBuilder caseLogsResponseBuilder, CaseLogConfiguration configuration) {
        this.caseLogService = caseLogService;
        this.caseLogsResponseBuilder = caseLogsResponseBuilder;
        this.configuration = configuration;
    }

    @RequestMapping(value = "all", method = GET)
    public void showAllLogs(HttpServletResponse response) throws IOException {
        List<CaseLog> allCaseLogs = caseLogService.getLatestLogs(configuration.getCaseLogsLimit());
        String caseLogsResponse = caseLogsResponseBuilder.createResponseMessage(allCaseLogs, CaseLogsController.VIEW_PATH);
        response.getOutputStream().print(caseLogsResponse);
    }

    @RequestMapping(value = "filter", method = POST)
    public void filterLogs(@RequestParam("entityId") String entityId, @RequestParam("requestType") String requestType, HttpServletResponse response) throws IOException {
        List<CaseLog> allCaseLogs = caseLogService.filter(entityId, requestType, configuration.getCaseLogsLimit());
        String caseLogsResponse = caseLogsResponseBuilder.createResponseMessage(allCaseLogs, CONTENT_PATH);

        response.getOutputStream().print(caseLogsResponse);
    }
}
