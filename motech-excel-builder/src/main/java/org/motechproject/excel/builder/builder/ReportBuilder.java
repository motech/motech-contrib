package org.motechproject.excel.builder.builder;

import org.motechproject.excel.builder.service.QueryService;
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
    private final QueryService queryService;
    private ExcelReportBuilder excelReportBuilder;

    public static final String DATA = "data";

    @Autowired
    public ReportBuilder(QueryService queryService, ExcelReportBuilder excelReportBuilder) {
        this.queryService = queryService;
        this.excelReportBuilder = excelReportBuilder;
    }

    public void buildReport(String reportType, OutputStream outputStream) {
        excelReportBuilder.build(outputStream, getReportData(reportType), TEMPLATE_FILES_PATH + reportType + TEMPLATE_FILE_EXTENSION);
    }

    private Map<String, Object> getReportData(String reportType) {
        List<Map<String,Object>> data = queryService.getData(reportType);
        return setReportParameters(data);
    }

    private Map<String, Object> setReportParameters(List<Map<String, Object>> data) {
        Map<String, Object> params = new HashMap<>();

        params.put(DATA, data);
        return params;
    }
}

