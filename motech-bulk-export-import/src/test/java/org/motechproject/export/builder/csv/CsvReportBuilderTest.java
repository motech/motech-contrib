package org.motechproject.export.builder.csv;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.export.model.ReportData;
import org.motechproject.export.model.ExcelReportDataSource;

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
public class CsvReportBuilderTest{

    @Mock
    private ExcelReportDataSource excelReportDataSource;

    @Test
    public void shouldReturnAFileThatHasCSVData() throws IOException {
        String reportName = "csv_report";
        String fileName = "report_file";
        List<String> columnHeaders = Arrays.asList("Header1", "Header2");
        List<List<String>> allRowData = Arrays.asList(Arrays.asList("Flw1", "Location1"), Arrays.asList("Flw2", "Location2"));
        Map<String, String> criteria = new HashMap<String, String>();
        when(excelReportDataSource.createEntireReport(reportName, criteria)).thenReturn(new ReportData(columnHeaders,allRowData));

        CsvReportBuilder csvReportBuilder = new CsvReportBuilder(fileName, reportName, excelReportDataSource, criteria);
        File file = csvReportBuilder.build();

        FileInputStream fileInputStream = new FileInputStream(file);
        String fileContents = IOUtils.toString(fileInputStream);
        String expectedString  =  "Header1,Header2\nFlw1,Location1\nFlw2,Location2\n";
        assertEquals(fileName, file.getName());
        assertEquals(expectedString, fileContents);
        FileUtils.deleteQuietly(new File(fileName));
    }

    @Test
    public void shouldCreateAnOutputFileWithDefaultNameIfNotGiven() throws IOException {
        String reportName = "csv_report";
        List<String> columnHeaders = Arrays.asList("Header1", "Header2");
        List<List<String>> allRowData = Arrays.asList(Arrays.asList("Flw1", "Location1"), Arrays.asList("Flw2", "Location2"));
        Map<String, String> criteria = new HashMap<String, String>();
        when(excelReportDataSource.createEntireReport(reportName, criteria)).thenReturn(new ReportData(columnHeaders,allRowData));
        when(excelReportDataSource.name()).thenReturn("SampleData");

        CsvReportBuilder csvReportBuilder = new CsvReportBuilder(null, reportName, excelReportDataSource, criteria);
        File file = csvReportBuilder.build();

        FileInputStream fileInputStream = new FileInputStream(file);
        String fileContents = IOUtils.toString(fileInputStream);
        String expectedString  =  "Header1,Header2\nFlw1,Location1\nFlw2,Location2\n";
        assertEquals("SampleData-report.csv", file.getName());
        assertEquals(expectedString, fileContents);
        FileUtils.deleteQuietly(new File("SampleData-report.csv"));
    }
}
