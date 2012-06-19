package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.motechproject.export.model.ExcelExportProcessor;
import org.motechproject.export.service.sample.SampleExcelDataSource;

import static org.junit.Assert.assertEquals;

public class PagedExcelBuilderTest {

    @Test
    public void shouldAddTitle() {
        HSSFWorkbook workbook = new PagedExcelBuilder(new ExcelExportProcessor(new SampleExcelDataSource()), "sampleExcel").build();
        assertEquals("Sample Excel", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldAddColumnHeaders() {
        HSSFWorkbook workbook = new PagedExcelBuilder(new ExcelExportProcessor(new SampleExcelDataSource()), "sampleExcel").build();
        assertEquals("Id", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldPageRowData() {
        HSSFWorkbook workbook = new PagedExcelBuilder(new ExcelExportProcessor(new SampleExcelDataSource()), "sampleExcel").build();
        assertDataInFirstPageAdded(workbook);
        assertDataInSecondPageAdded(workbook);
    }

    private void assertDataInFirstPageAdded(HSSFWorkbook workbook) {
        assertEquals("id1", workbook.getSheetAt(0).getRow(2).getCell(0).getStringCellValue());
        assertEquals("id2", workbook.getSheetAt(0).getRow(3).getCell(0).getStringCellValue());
    }

    private void assertDataInSecondPageAdded(HSSFWorkbook workbook) {
        assertEquals("id3", workbook.getSheetAt(0).getRow(4).getCell(0).getStringCellValue());
    }
}
