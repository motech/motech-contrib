package org.motechproject.export.builder.excel.model;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public class Workbook {

    private final HSSFWorkbook workbook;
    private Worksheet worksheet;
    private String title;
    private List<String> columnHeaders;
    private int worksheetNumber;
    private List<String> customHeaders;
    private List<String> customFooters;
    private List<String> columnFormats;

    public Workbook(String title, List<String> columnHeaders, List<String> customHeaders, List<String> customFooters, List<String> columnFormats) {
        this.title = title;
        this.columnHeaders = columnHeaders;
        this.customHeaders = customHeaders;
        this.customFooters = customFooters;
        this.columnFormats = columnFormats;
        workbook = new HSSFWorkbook();

        worksheetNumber = 0;
        worksheet = newWorksheet();
    }

    public void addRow(List<Object> rowData) {
        if (!worksheet.addRow(rowData)) {
            worksheet = newWorksheet();
            worksheet.addRow(rowData);
        }
    }

    public HSSFWorkbook book() {
        return workbook;
    }

    private Worksheet newWorksheet() {
        return new Worksheet(workbook, "Worksheet" + (worksheetNumber++), title, columnHeaders, customHeaders, customFooters, columnFormats);
    }

    public void addCustomFooter() {
        worksheet.addCustomFooter();
    }

    public void autoResizeAllColumns() {
        worksheet.autoResizeAllColumns();
    }
}
