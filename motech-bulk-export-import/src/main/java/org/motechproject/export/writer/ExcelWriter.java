package org.motechproject.export.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.PagedExcelBuilder;
import org.motechproject.export.model.ExcelExportProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class ExcelWriter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void writeExcelToResponse(OutputStream outputStream, ExcelExportProcessor excelExportProcessor, String reportName, String fileName) {
        try {
            HSSFWorkbook excelWorkbook = createExcelWorkBook(excelExportProcessor, reportName);
            if (null != excelWorkbook)
                excelWorkbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response",  e);
        }
    }

    private HSSFWorkbook createExcelWorkBook(ExcelExportProcessor excelExportProcessor, String reportName) {
        try {
            return new PagedExcelBuilder(excelExportProcessor, reportName).build();
        } catch (Exception e) {
            logger.error("Error while generating excel report", e);
            return null;
        }
    }
}
