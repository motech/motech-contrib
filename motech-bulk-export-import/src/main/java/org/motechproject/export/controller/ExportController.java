package org.motechproject.export.controller;

import org.apache.log4j.Logger;
import org.motechproject.export.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(value = "/reports")
@Controller
@Deprecated
public class ExportController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

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

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception ex) {
        logger.error("Error occurred", ex);
        return "Error occurred while exporting.";
    }

}
