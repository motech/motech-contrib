package org.motechproject.export.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.PagedReportBuilder;
import org.motechproject.export.model.ExcelReportDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExcelWriter {

    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void writeExcelToResponse(HttpServletResponse response, ExcelReportDataSource excelReportDataSource, String reportName, String fileName) {
        try {
            initializeExcelResponse(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            HSSFWorkbook excelWorkbook = createExcelWorkBook(excelReportDataSource, reportName);
            if (null != excelWorkbook)
                excelWorkbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response: " + e.getMessage());
        }
    }


    private HSSFWorkbook createExcelWorkBook(ExcelReportDataSource excelReportDataSource, String reportName) {
        try {
            return new PagedReportBuilder(excelReportDataSource, reportName).build();
        } catch (Exception e) {
            logger.error("Error while generating excel report: " + e.getMessage());
            return null;
        }
    }


    private void initializeExcelResponse(HttpServletResponse response, String fileName) {
        response.setHeader(CONTENT_DISPOSITION, "inline; filename=" + fileName);
        response.setContentType(APPLICATION_VND_MS_EXCEL);
    }
}
