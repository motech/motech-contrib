package org.motechproject.export.builder.excel.model;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class WorksheetTest {

    @Test
    public void shouldAddRowWhenNumberOfRowsDoesNotExceedLimit() {
        Worksheet worksheet = new Worksheet(new HSSFWorkbook(), "sheet", "Test", asList("Column"), Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<String>emptyList());
        boolean addedSheet = true;
        for (int i = 0; i <= maxDataRowIndex(); i++) {
            addedSheet &= worksheet.addRow(asList((Object)"test"));
        }
        assertTrue(addedSheet);
    }

    @Test
    public void shouldNotAddRowWhenNumberOfRowsDoesNotExceedLimit() {
        List<Object> rowData = asList((Object) "test");
        Worksheet worksheet = new Worksheet(new HSSFWorkbook(), "sheet", "Test", asList("Column"), Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<String>emptyList());
        for (int i = 0; i <= maxDataRowIndex(); i++) {
            worksheet.addRow(rowData);
        }
        assertFalse(worksheet.addRow(rowData));
        assertNull(worksheet.sheet.getRow(Worksheet.MAX_ROW_INDEX + 1));
    }

    private int maxDataRowIndex() {
        return Worksheet.MAX_ROW_INDEX - Worksheet.HEADER_ROW_COUNT;
    }
}
