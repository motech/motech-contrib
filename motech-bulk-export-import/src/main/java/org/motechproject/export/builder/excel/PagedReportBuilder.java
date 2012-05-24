package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.export.builder.excel.model.Workbook;
import org.motechproject.export.model.ReportData;
import org.motechproject.export.model.ReportDataSource;

import java.util.List;

public class PagedReportBuilder {

    private Workbook workbook;

    private ReportDataSource reportDataSource;
    private String reportName;

    public PagedReportBuilder(ReportDataSource reportDataSource, String reportName) {
        this.reportDataSource = reportDataSource;
        this.reportName = reportName;
    }

    public HSSFWorkbook build() {
        ReportData pagedReport = reportDataSource.createPagedReport(reportName);
        workbook = new Workbook(reportDataSource.title(), pagedReport.getColumnHeaders());
        for(List<String> row : pagedReport.getAllRowData())
            workbook.addRow(row);
        return workbook.book();
    }

}
