package org.motechproject.export.builder.excel.model;

import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;

public class WorkbookTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAddSheetWhenDataFitsIntoCurrentSheet() {
        Workbook workbook = new Workbook("Test", asList("Column"), Collections.<String>emptyList(), Collections.<String>emptyList());
        for (int i = 0; i <= Worksheet.MAX_ROW_INDEX - Worksheet.HEADER_ROW_COUNT; i++) {
            workbook.addRow(asList("test"));
        }
        workbook.book().getSheetAt(1);
    }

    @Test
    public void shouldAddSheetWhenDataExceedsTheLimitOfCurrentSheet() {
        Workbook workbook = new Workbook("Test", asList("Column"), Collections.<String>emptyList(), Collections.<String>emptyList());
        for (int i = 0; i <= Worksheet.MAX_ROW_INDEX - Worksheet.HEADER_ROW_COUNT; i++) {
            workbook.addRow(asList("test"));
        }
        workbook.addRow(asList("test"));
        assertNotNull(workbook.book().getSheetAt(1));
    }
}
