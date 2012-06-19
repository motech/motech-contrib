package org.motechproject.export.model;

import org.junit.Test;
import org.motechproject.export.annotation.ExcelReportGroup;
import org.motechproject.export.annotation.Report;
import org.motechproject.export.controller.sample.SampleData;
import org.motechproject.export.controller.sample.SampleExcelReportController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

public class ExcelReportDataSourceTest {

    @Test
    public void isValidDataSourceIfAnnotatedWithReport() {
        assertTrue(ExcelReportDataSource.isValidDataSource(SampleExcelReportController.class));
    }

    @Test
    public void isNotValidDataSourceIfNotAnnotatedWithReport() {
        assertFalse(ExcelReportDataSource.isValidDataSource(ExcelReportDataSourceTest.class));
    }

    @Test
    public void nameIsSpecifiedInReportAnnotation() {
        assertEquals("sampleExcelReports", new ExcelReportDataSource(new SampleExcelReportController()).name());
    }

    @Test
    public void shouldRetrieveDataFromDataSource() {
        assertArrayEquals(
                new SampleData[]{new SampleData("id1"), new SampleData("id2")},
                new ExcelReportDataSource(new SampleExcelReportController()).dataForPage("sampleExcelReports", 1).toArray()
        );
    }

    @Test
    public void shouldRetrieveEmptyListWhenDataMethodIsNotSpecified() {
        assertEquals(0, new ExcelReportDataSource(new WithoutDataMethod()).dataForPage("sampleExcelReports", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodSpecifiedIsNotPublic() {
        assertEquals(0, new ExcelReportDataSource(new WithPrivateDataMethod()).dataForPage("sampleExcelReports", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotHavePageNumber() {
        assertEquals(0, new ExcelReportDataSource(new WithInvalidDataMethod()).dataForPage("sampleExcelReports", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotReturnAList() {
        assertEquals(0, new ExcelReportDataSource(new WithInvalidReturnType()).dataForPage("sampleExcelReports", 1).size());
    }

    @Test
    public void shouldReturnNameForTitle() {
        assertEquals("Without Data Method", new ExcelReportDataSource(new WithoutDataMethod()).title());
    }

    @Test
    public void shouldReturnColumnHeaders() {
        assertEquals(asList("Id", "Custom column name", "Boolean Value"), new ExcelReportDataSource(new ValidReportDataSource()).columnHeaders("sampleExcelReports"));
    }

    @Test
    public void shouldCreateAnEntireReportWithHeadersAndRows() {
        ExcelReportDataSource excelReportDataSource = new ExcelReportDataSource(new ValidReportDataSource());
        Map<String, String> criteria = new HashMap<String, String>();
        ReportData report = excelReportDataSource.createEntireReport("sampleExcelReports", criteria);

        List<String> columnHeaders = report.getColumnHeaders();
        List<List<String>> allRowData = report.getAllRowData();

        assertTrue(columnHeaders.size() == 3);
        assertEquals("Id", columnHeaders.get(0));
        assertEquals("Custom column name", columnHeaders.get(1));
        assertEquals("Boolean Value", columnHeaders.get(2));
        assertTrue(allRowData.size() == 1);
        assertTrue(allRowData.get(0).contains("id"));
        assertTrue(allRowData.get(0).contains("title"));
        assertTrue(allRowData.get(0).contains("true"));
    }

    @Test
    public void shouldCreateAPagedReportWithHeadersAndRows() {
        ExcelReportDataSource excelReportDataSource = new ExcelReportDataSource(new ValidPagedReportDataSource());
        ReportData report = excelReportDataSource.createPagedReport("sampleExcelReports");

        List<String> columnHeaders = report.getColumnHeaders();
        List<List<String>> allRowData = report.getAllRowData();

        assertTrue(columnHeaders.size() == 3);
        assertEquals("Id", columnHeaders.get(0));
        assertEquals("Custom column name", columnHeaders.get(1));
        assertEquals("Boolean Value", columnHeaders.get(2));
        assertTrue(allRowData.size() == 1);
        assertTrue(allRowData.get(0).contains("id"));
        assertTrue(allRowData.get(0).contains("title"));
        assertTrue(allRowData.get(0).contains("true"));
    }

}

@ExcelReportGroup(name = "validReportDataSource")
class ValidReportDataSource {

    @Report
    public List<SampleData> sampleExcelReports(Map<String, String> criteria) {
        return asList(new SampleData("id"));
    }
}

@ExcelReportGroup(name = "validPagedReportDataSource")
class ValidPagedReportDataSource {

    @Report
    public List<SampleData> sampleExcelReports(int pageNumber) {
        if (pageNumber == 2)
            return null;
        return asList(new SampleData("id"));
    }
}


@ExcelReportGroup(name = "withoutDataMethod")
class WithoutDataMethod {
}

@ExcelReportGroup(name = "withPrivateDataMethod")
class WithPrivateDataMethod {

    @Report
    private List<SampleData> sampleExcelReports(int pageNumber) {
        return asList(new SampleData("id"));
    }

}

@ExcelReportGroup(name = "withInvalidDataMethod")
class WithInvalidDataMethod {

    @Report
    public List<SampleData> sampleExcelReports() {
        return asList(new SampleData("id"));
    }

}

@ExcelReportGroup(name = "withInvalidReturnType")
class WithInvalidReturnType {

    @Report
    public SampleData sampleExcelReports() {
        return new SampleData("id");
    }
}

