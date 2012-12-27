package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class ExcelBuilderTest {

    public static class TestExcelBuilder extends ExcelBuilder<String> {

        protected TestExcelBuilder() {
            super("Test", asList("Column 1", "Column 2"), Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<String>emptyList());
        }

        @Override
        protected List<Object> createRowData(String modal) {
            return asList((Object)modal,(Object) modal);
        }

        @Override
        protected List<String> data() {
            return asList("Row1", "Row2");
        }
    }

    @Test
    public void shouldAddTitle() {
        HSSFWorkbook workbook = new TestExcelBuilder().build();
        assertEquals("Test", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldAddColumnHeaders() {
        HSSFWorkbook workbook = new TestExcelBuilder().build();
        assertEquals("Column 1", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
        assertEquals("Column 2", workbook.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
    }

    @Test
    public void shouldAddRowData() {
        HSSFWorkbook workbook = new TestExcelBuilder().build();
        assertEquals("Row1", workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        assertEquals("Row2", workbook.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
    }
}
