package org.motechproject.excel.builder.controller;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.excel.builder.builder.ReportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/download")
public class ReportsController {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    public static final String TEMPLATE_FILE_EXTENSION = ".xls";


    private ReportBuilder reportBuilder;

    @Autowired
    public ReportsController(ReportBuilder reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

    @RequestMapping(value = "/{reportType}.xls", method = RequestMethod.GET)
    @ResponseBody
    public void log(@PathVariable String reportType, @RequestParam(required = false, defaultValue = "{}") String filterParams, HttpServletResponse response) throws IOException {
        initializeExcelResponse(response, reportType);
        reportBuilder.buildReport(reportType, getFilterParams(filterParams), response.getOutputStream());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(RuntimeException ex) {
        logger.error("Error occurred", ex);
        throw ex;
    }

    private void initializeExcelResponse(HttpServletResponse response, String reportType) {
        response.setHeader(CONTENT_DISPOSITION, "inline; filename=" + reportType + TEMPLATE_FILE_EXTENSION);
        response.setContentType(APPLICATION_VND_MS_EXCEL);
    }

    private FilterParams getFilterParams(String filterParams) throws IOException {
        return new ObjectMapper().readValue(filterParams, FilterParams.class).removeEmptyParams();
    }

}
