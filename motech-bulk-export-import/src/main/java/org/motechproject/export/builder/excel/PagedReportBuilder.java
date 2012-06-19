package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.model.Workbook;
import org.motechproject.export.model.ReportData;
import org.motechproject.export.model.ExcelReportDataSource;

import java.util.List;

public class PagedReportBuilder {

    private Workbook workbook;

    private ExcelReportDataSource excelReportDataSource;
    private String reportName;

    public PagedReportBuilder(ExcelReportDataSource excelReportDataSource, String reportName) {
        this.excelReportDataSource = excelReportDataSource;
        this.reportName = reportName;
    }

    public HSSFWorkbook build() {
        ReportData pagedReport = excelReportDataSource.createPagedReport(reportName);
        workbook = new Workbook(excelReportDataSource.title(), pagedReport.getColumnHeaders());
        for(List<String> row : pagedReport.getAllRowData())
            workbook.addRow(row);
        return workbook.book();
    }

}
