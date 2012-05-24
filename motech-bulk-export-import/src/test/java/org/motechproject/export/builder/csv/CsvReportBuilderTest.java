package org.motechproject.export.builder.csv;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.export.model.ReportData;
import org.motechproject.export.model.ReportDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CsvReportBuilderTest{

    @Mock
    private ReportDataSource reportDataSource;

    @Test
    public void shouldReturnAFileThatHasCSVData() throws IOException {
        String reportName = "csv_report";
        String fileName = "report_file";
        List<String> columnHeaders = Arrays.asList("Header1", "Header2");
        List<List<String>> allRowData = Arrays.asList(Arrays.asList("Flw1", "Location1"), Arrays.asList("Flw2", "Location2"));
        int pageNumber = 1;
        when(reportDataSource.createEntireReport(reportName)).thenReturn(new ReportData(columnHeaders,allRowData));
        CsvReportBuilder csvReportBuilder = new CsvReportBuilder(fileName, reportName, reportDataSource);
        File file = csvReportBuilder.build();
        FileInputStream fileInputStream = new FileInputStream(file);
        String fileContents = IOUtils.toString(fileInputStream);
        String expectedString  =  "Header1,Header\nFlw1,Location\nFlw2,Location\n";
        assertEquals(expectedString, fileContents);
        FileUtils.deleteQuietly(new File(fileName));
    }
}
