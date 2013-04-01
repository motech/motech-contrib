package org.motechproject.excel.builder.builder;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.motechproject.excel.builder.service.ExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Component
public class ExcelReportBuilder {
    public static final String GENERATED_ON = "generatedOn";
    private final ExcelExporter excelExporter;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public ExcelReportBuilder(ExcelExporter excelExporter) {
        this.excelExporter = excelExporter;
    }

    void build(OutputStream outputStream, Map params, String templateFileName) {
        try {
            Workbook workbook = excelExporter.export(templateFileName, params);
            if (workbook != null) {
                workbook.write(outputStream);
            }
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response: " + e.getMessage());
        }
    }

}