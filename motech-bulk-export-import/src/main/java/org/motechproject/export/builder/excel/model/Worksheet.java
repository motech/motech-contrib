package org.motechproject.export.builder.excel.model;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

public class Worksheet {

    public static final int TITLE_ROW_HEIGHT = 500;
    public static final int MAX_ROW_INDEX = 29999;
    public static final int HEADER_ROW_COUNT = 2;
    public static final int HEADER_ROW_HEIGHT = 500;
    public static final int FIRST_COLUMN = 0;

    private int currentRowIndex = 0;
    private List<String> columnHeaders;
    private List<String> customFooters;
    HSSFSheet sheet;
    private String title;
    private List<String> customHeaders;

    public Worksheet(HSSFWorkbook workbook, String sheetName, String title, List<String> columnHeaders, List<String> customHeaders, List<String> customFooters) {
        this.title = title;
        this.customHeaders = customHeaders;
        this.columnHeaders = columnHeaders;
        this.customFooters = customFooters;
        sheet = workbook.createSheet(sheetName);
        initializeLayout();
        sheet.createFreezePane(0, currentRowIndex);
    }

    public boolean addRow(List<String> rowData) {
        if (dataRowIndex() > lastDataRowIndex()) {
            addCustomFooter();
            return false;
        } else {
            HSSFRow row = sheet.createRow(currentRowIndex);
            for (int i = 0; i < rowData.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(rowData.get(i));
            }
            currentRowIndex++;
        }
        return true;
    }

    private int dataRowIndex() {
        return currentRowIndex - getHeaderCount();
    }

    private int getHeaderCount() {
        return HEADER_ROW_COUNT + (customHeaders != null && !customHeaders.isEmpty() ? customHeaders.size() + 1 : 0);
    }

    private int lastDataRowIndex() {
        return MAX_ROW_INDEX - (getHeaderCount());
    }

    protected void initializeLayout() {
        buildTitle(title, columnHeaders.size(), CellStyle.ALIGN_CENTER);
        buildCustomHeaders();
        buildColumnNamesHeader();
    }

    private void buildTitle(String title, int width, short alignment) {
        HSSFRow rowTitle = sheet.createRow((short) currentRowIndex);
        rowTitle.setHeight((short) TITLE_ROW_HEIGHT);

        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(new MotechCellStyle(sheet, alignment).style());

        sheet.addMergedRegion(new CellRangeAddress(currentRowIndex, currentRowIndex, FIRST_COLUMN, width));
        currentRowIndex++;
    }

    private void buildColumnNamesHeader() {
        HSSFCellStyle headerCellStyle = new MotechCellStyle(sheet).style();

        HSSFRow headerRow = sheet.createRow((short) currentRowIndex);
        headerRow.setHeight((short) HEADER_ROW_HEIGHT);

        int columnIndex = 0;
        for (ExcelColumn column : columnHeaders()) {
            HSSFCell cell = headerRow.createCell(columnIndex);
            cell.setCellValue(column.getHeader());
            cell.setCellStyle(headerCellStyle);
            columnIndex++;
        }
        currentRowIndex++;
    }

    private void buildCustomHeaders() {
        if (this.customHeaders != null && !this.customHeaders.isEmpty()) {
            buildTitle("", columnHeaders.size(), CellStyle.ALIGN_LEFT);
            for (String headerValue : this.customHeaders) {
                buildTitle(headerValue, columnHeaders.size(), CellStyle.ALIGN_LEFT);
            }
        }
    }

    private List<ExcelColumn> columnHeaders() {
        List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
        for (String header : columnHeaders) {
            columns.add(new ExcelColumn(header, Cell.CELL_TYPE_STRING, 8000));
        }
        setColumnWidths(columns);
        return columns;
    }

    private void setColumnWidths(List<ExcelColumn> columns) {
        int columnIndex = 0;
        for (ExcelColumn column : columns) {
            sheet.setColumnWidth(columnIndex, column.getWidth());
            columnIndex++;
        }
    }

    public void addCustomFooter() {
        if (this.customFooters != null && !this.customFooters.isEmpty()) {
            buildTitle("", columnHeaders.size(), CellStyle.ALIGN_LEFT);
            for (String headerValue : this.customFooters) {
                buildTitle(headerValue, columnHeaders.size(), CellStyle.ALIGN_LEFT);
            }
        }
    }
}
