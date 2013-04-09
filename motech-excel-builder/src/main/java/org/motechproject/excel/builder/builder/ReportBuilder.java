package org.motechproject.excel.builder.builder;

import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.excel.builder.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportBuilder {

    public static final String TEMPLATE_FILES_PATH = "/xls/templates/";
    public static final String TEMPLATE_FILE_EXTENSION = ".xls";
    private final ReportService reportService;
    private ExcelReportBuilder excelReportBuilder;

    public static final String DATA = "data";

    @Autowired
    public ReportBuilder(ReportService reportService, ExcelReportBuilder excelReportBuilder) {
        this.reportService = reportService;
        this.excelReportBuilder = excelReportBuilder;
    }

    public void buildReport(String reportType, FilterParams filterParams, OutputStream outputStream) {
        excelReportBuilder.build(outputStream, getReportData(reportType, filterParams), TEMPLATE_FILES_PATH + reportType + TEMPLATE_FILE_EXTENSION);
    }

    private Map<String, Object> getReportData(String reportType, FilterParams filterParams) {
        List<Map<String,Object>> data = reportService.getData(reportType, filterParams);
        return setReportParameters(data);
    }

    private Map<String, Object> setReportParameters(List<Map<String, Object>> data) {
        Map<String, Object> params = new HashMap<>();

        params.put(DATA, data);
        return params;
    }
}

