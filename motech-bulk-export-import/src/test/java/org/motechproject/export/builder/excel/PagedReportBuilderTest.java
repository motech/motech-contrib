package org.motechproject.export.builder.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.motechproject.export.controller.sample.SampleExcelReportController;
import org.motechproject.export.model.ExcelReportDataSource;

import static org.junit.Assert.assertEquals;

public class PagedReportBuilderTest {

    @Test
    public void shouldAddTitle() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ExcelReportDataSource(new SampleExcelReportController()), "sampleExcelReports").build();
        assertEquals("Sample Excel Reports", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldAddColumnHeaders() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ExcelReportDataSource(new SampleExcelReportController()), "sampleExcelReports").build();
        assertEquals("Id", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
    }

    @Test
    public void shouldPageRowData() {
        HSSFWorkbook workbook = new PagedReportBuilder(new ExcelReportDataSource(new SampleExcelReportController()), "sampleExcelReports").build();
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
