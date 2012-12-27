package org.motechproject.export.builder.csv;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.export.model.ExcelExportProcessor;
import org.motechproject.export.model.ExportData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CSVBuilderTest {

    @Mock
    private ExcelExportProcessor excelExportProcessor;

    @Test
    public void shouldReturnAFileThatHasCSVData() throws IOException {
        String reportName = "csv_report";
        String fileName = "report_file";
        List<String> columnHeaders = Arrays.asList("Header1", "Header2");
        List<List<Object>> allRowData =  Arrays.asList(Arrays.asList((Object) "Flw1", (Object)"Location1"), Arrays.asList((Object)"Flw2",(Object) "Location2"));
        Map<String, String> criteria = new HashMap<String, String>();
        when(excelExportProcessor.getEntireExcelData(reportName, criteria)).thenReturn(new ExportData(columnHeaders, allRowData));

        CSVBuilder CSVBuilder = new CSVBuilder(fileName, reportName, excelExportProcessor, criteria);
        File file = CSVBuilder.build();

        FileInputStream fileInputStream = new FileInputStream(file);
        String fileContents = IOUtils.toString(fileInputStream);
        String expectedString = "Header1,Header2\nFlw1,Location1\nFlw2,Location2\n";
        assertEquals(fileName, file.getName());
        assertEquals(expectedString, fileContents);
        FileUtils.deleteQuietly(new File(fileName));
    }

    @Test
    public void shouldCreateAnOutputFileWithDefaultNameIfNotGiven() throws IOException {
        String reportName = "csv_report";
        List<String> columnHeaders = Arrays.asList("Header1", "Header2");
        List<List<Object>> allRowData = Arrays.asList(Arrays.asList((Object)"Flw1", (Object)"Location1"), Arrays.asList((Object)"Flw2", (Object)"Location2"));
        Map<String, String> criteria = new HashMap<String, String>();
        when(excelExportProcessor.getEntireExcelData(reportName, criteria)).thenReturn(new ExportData(columnHeaders, allRowData));
        when(excelExportProcessor.name()).thenReturn("SampleData");

        CSVBuilder CSVBuilder = new CSVBuilder(null, reportName, excelExportProcessor, criteria);
        File file = CSVBuilder.build();

        FileInputStream fileInputStream = new FileInputStream(file);
        String fileContents = IOUtils.toString(fileInputStream);
        String expectedString = "Header1,Header2\nFlw1,Location1\nFlw2,Location2\n";
        assertEquals("SampleData-report.csv", file.getName());
        assertEquals(expectedString, fileContents);
        FileUtils.deleteQuietly(new File("SampleData-report.csv"));
    }
}
