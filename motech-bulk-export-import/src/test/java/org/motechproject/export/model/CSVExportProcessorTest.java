package org.motechproject.export.model;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CSVExportProcessorTest {
    
    @Test
    public void shouldReturnColumnHeadersEvenWhenThereIsNoRowData(){
        DummyList dummyList = new DummyList(DummyCsv.class);
        ExportData exportData = new CSVExportProcessor().formatCSVData(dummyList);
        List<String> columnHeaders = exportData.getColumnHeaders();
        assertEquals(2,columnHeaders.size());
        assertEquals("district",columnHeaders.get(0));
        assertEquals("panchy",columnHeaders.get(1));
    }
}


