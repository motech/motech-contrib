package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.model.Workbook;
import org.motechproject.export.model.ExcelExportProcessor;
import org.motechproject.export.model.ExportData;

import java.util.List;

public class PagedExcelBuilder {

    private Workbook workbook;

    private ExcelExportProcessor excelExportProcessor;
    private String reportName;

    public PagedExcelBuilder(ExcelExportProcessor excelExportProcessor, String reportName) {
        this.excelExportProcessor = excelExportProcessor;
        this.reportName = reportName;
    }

    public HSSFWorkbook build() {
        ExportData pagedExport = excelExportProcessor.getPaginatedExcelData(reportName);
        workbook = new Workbook(excelExportProcessor.title(), pagedExport.getColumnHeaders());
        for (List<String> row : pagedExport.getAllRowData())
            workbook.addRow(row);
        return workbook.book();
    }

}
