package org.motechproject.excel.builder.it;

import bad.robot.excel.valuetypes.Coordinate;
import bad.robot.excel.valuetypes.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.motechproject.excel.builder.service.ExcelExporter;

import java.io.IOException;
import java.util.Date;

import static bad.robot.excel.valuetypes.Coordinate.coordinate;

public abstract class ExcelTest {

    protected final ExcelExporter excelExporter = new ExcelExporter();

    protected org.apache.poi.ss.usermodel.Cell getCellForCoordinate(Coordinate coordinate, Workbook workbook) throws IOException {
        org.apache.poi.ss.usermodel.Row row = getRowForCoordinate(coordinate, workbook);
        return row.getCell(coordinate.getColumn().value());
    }

    protected org.apache.poi.ss.usermodel.Row getRowForCoordinate(Coordinate coordinate, Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(coordinate.getSheet().value());
        org.apache.poi.ss.usermodel.Row row = sheet.getRow(coordinate.getRow().value());
        if (row == null)
            throw new IllegalStateException("expected to find a row");
        return row;
    }

    String stringValue(ExcelColumnIndex columnIndex, int rowNumber) throws IOException {
        return getCellForCoordinate(coordinate(columnIndex, rowNumber), getWorkbook()).getStringCellValue();
    }

    Date dateValue(ExcelColumnIndex columnIndex, int rowNumber) throws IOException {
        return getCellForCoordinate(coordinate(columnIndex, rowNumber), getWorkbook()).getDateCellValue();
    }

    double numericValue(ExcelColumnIndex columnIndex, int rowNumber) throws IOException {
        return getCellForCoordinate(coordinate(columnIndex, rowNumber), getWorkbook()).getNumericCellValue();
    }

    abstract Workbook getWorkbook();
}
