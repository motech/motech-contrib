package org.motechproject.export.controller;

import org.motechproject.export.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(value = "/reports")
@Controller
@Deprecated
public class ExportController {

    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String TEXT_CSV = "text/csv";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

    @Autowired
    private ExportService exportService;

    @RequestMapping(method = RequestMethod.GET, value = "{groupName}/{reportName}.xls")
    public void createExcelReport(@PathVariable("groupName") String groupName, @PathVariable("reportName") String reportName,
                                  HttpServletResponse response) throws IOException {
        initializeExcelResponse(response, reportName + ".xls");
        exportService.exportAsExcel(groupName, reportName, response.getOutputStream());
    }

    @RequestMapping(method = RequestMethod.GET, value = "{groupName}/{reportName}.csv")
    public void createCSVReport(@PathVariable("groupName") String groupName, @PathVariable("reportName") String reportName,
                                HttpServletResponse response) throws IOException {
        initializeCSVResponse(response, reportName + ".csv");
        exportService.exportAsCSV(groupName, response.getWriter());
    }

    private void initializeExcelResponse(HttpServletResponse response, String fileName) {
        response.setHeader(CONTENT_DISPOSITION, "inline; filename=" + fileName);
        response.setContentType(APPLICATION_VND_MS_EXCEL);
    }


    private void initializeCSVResponse(HttpServletResponse response, String fileName) {
        response.setHeader(CONTENT_DISPOSITION, "inline; filename=" + fileName);
        response.setContentType(TEXT_CSV);
    }

}
