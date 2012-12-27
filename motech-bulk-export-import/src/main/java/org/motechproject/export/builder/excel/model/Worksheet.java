
package org.motechproject.export.builder.excel.model;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Worksheet {

    public static final int TITLE_ROW_HEIGHT = 500;
    public static final int MAX_ROW_INDEX = 29999;
    public static final int HEADER_ROW_COUNT = 2;
    public static final int HEADER_ROW_HEIGHT = 500;
    public static final int FIRST_COLUMN = 0;
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

    private int currentRowIndex = 0;
    private List<String> columnHeaders;
    private List<String> customFooters;
    private List<String> columnFormats;
    HSSFSheet sheet;
    private String title;
    private List<String> customHeaders;
    private HSSFWorkbook workbook;

    public Worksheet(HSSFWorkbook workbook, String sheetName, String title, List<String> columnHeaders, List<String> customHeaders, List<String> customFooters, List<String> columnFormats) {
        this.title = title;
        this.customHeaders = customHeaders;
        this.columnHeaders = columnHeaders;
        this.customFooters = customFooters;
        this.columnFormats = columnFormats;
        sheet = workbook.createSheet(sheetName);
        initializeLayout();
        sheet.createFreezePane(0, currentRowIndex);
        this.workbook = workbook;
    }

    public boolean addRow(List<Object> rowData) {
        if (dataRowIndex() > lastDataRowIndex()) {
            addCustomFooter();
            autoResizeAllColumns();
            return false;
        } else {
            HSSFRow row = sheet.createRow(currentRowIndex);
            for (int i = 0; i < rowData.size(); i++) {
                Object value = rowData.get(i);
                HSSFCell cell = row.createCell(i);
                if(value instanceof Date){
                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    String dateFormat = getDateFormat(columnFormats.get(i));
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) value);
                } else {
                    cell.setCellValue(value != null ? value.toString(): null);
                }
            }
            currentRowIndex++;
        }
        return true;
    }

    private String getDateFormat(String specifiedColumnFormat) {
        return specifiedColumnFormat != null && !specifiedColumnFormat.isEmpty() ? specifiedColumnFormat : DEFAULT_DATE_FORMAT;
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
        buildTitle(title, columnHeaders.size(), CellStyle.ALIGN_CENTER, true);
        buildCustomHeaders();
        buildColumnNamesHeader();
    }

    private void buildTitle(String title, int width, short alignment, boolean emphasize) {
        HSSFRow rowTitle = sheet.createRow((short) currentRowIndex);

        HSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue(title);
        if (emphasize) {
            cellTitle.setCellStyle(new MotechCellStyle(sheet, alignment).style());
            rowTitle.setHeight((short) TITLE_ROW_HEIGHT);
        }

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
            buildTitle("", columnHeaders.size(), CellStyle.ALIGN_LEFT, false);
            for (String headerValue : this.customHeaders) {
                buildTitle(headerValue, columnHeaders.size(), CellStyle.ALIGN_LEFT, false);
            }
        }
    }

    private List<ExcelColumn> columnHeaders() {
        List<ExcelColumn> columns = new ArrayList<ExcelColumn>();
        for (String header : columnHeaders) {
            columns.add(new ExcelColumn(header, Cell.CELL_TYPE_STRING));
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
            buildTitle("", columnHeaders.size(), CellStyle.ALIGN_LEFT, false);
            for (String headerValue : this.customFooters) {
                buildTitle(headerValue, columnHeaders.size(), CellStyle.ALIGN_LEFT, false);
            }
        }
    }

    public void autoResizeAllColumns() {
        for (int i = 0; i < columnHeaders.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
