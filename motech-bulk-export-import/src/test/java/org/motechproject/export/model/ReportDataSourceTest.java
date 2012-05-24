package org.motechproject.export.model;

import org.junit.Test;
import org.motechproject.export.annotation.Report;
import org.motechproject.export.annotation.ReportGroup;
import org.motechproject.export.controller.sample.SampleData;
import org.motechproject.export.controller.sample.SampleReportController;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class ReportDataSourceTest {

    @Test
    public void isValidDataSourceIfAnnotatedWithReport() {
        assertTrue(ReportDataSource.isValidDataSource(SampleReportController.class));
    }

    @Test
    public void isNotValidDataSourceIfNotAnnotatedWithReport() {
        assertFalse(ReportDataSource.isValidDataSource(ReportDataSourceTest.class));
    }

    @Test
    public void nameIsSpecifiedInReportAnnotation() {
        assertEquals("sampleReports", new ReportDataSource(new SampleReportController()).name());
    }

    @Test
    public void shouldRetrieveDataFromDataSource() {
        assertArrayEquals(
                new SampleData[]{new SampleData("id1"), new SampleData("id2")},
                new ReportDataSource(new SampleReportController()).dataForPage("sampleReport", 1).toArray()
        );
    }

    @Test
    public void shouldRetrieveEmptyListWhenDataMethodIsNotSpecified() {
        assertEquals(0, new ReportDataSource(new WithoutDataMethod()).dataForPage("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodSpecifiedIsNotPublic() {
        assertEquals(0, new ReportDataSource(new WithPrivateDataMethod()).dataForPage("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotHavePageNumber() {
        assertEquals(0, new ReportDataSource(new WithInvalidDataMethod()).dataForPage("sampleReport", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotReturnAList() {
        assertEquals(0, new ReportDataSource(new WithInvalidReturnType()).dataForPage("sampleReport", 1).size());
    }

    @Test
    public void shouldReturnNameForTitle() {
        assertEquals("Without Data Method", new ReportDataSource(new WithoutDataMethod()).title());
    }

    @Test
    public void shouldReturnColumnHeaders() {
        assertEquals(asList("Id", "Custom column name"), new ReportDataSource(new ValidReportDataSource()).columnHeaders("sampleReport"));
    }

    @Test
    public void shouldCreateAnEntireReportWithHeadersAndRows(){
        ReportDataSource reportDataSource = new ReportDataSource(new ValidReportDataSource());
        ReportData report = reportDataSource.createEntireReport("sampleReport");

        List<String> columnHeaders = report.getColumnHeaders();
        List<List<String>> allRowData = report.getAllRowData();

        assertTrue(columnHeaders.size() == 2);
        assertEquals("Id", columnHeaders.get(0));
        assertEquals("Custom column name",columnHeaders.get(1));
        assertTrue(allRowData.size()==1);
        assertTrue(allRowData.get(0).contains("id"));
        assertTrue(allRowData.get(0).contains("title"));
    }

    @Test
    public void shouldCreateAPagedReportWithHeadersAndRows(){
        ReportDataSource reportDataSource = new ReportDataSource(new ValidPagedReportDataSource());
        ReportData report = reportDataSource.createPagedReport("sampleReport");

        List<String> columnHeaders = report.getColumnHeaders();
        List<List<String>> allRowData = report.getAllRowData();

        assertTrue(columnHeaders.size() == 2);
        assertEquals("Id", columnHeaders.get(0));
        assertEquals("Custom column name",columnHeaders.get(1));
        assertTrue(allRowData.size()==1);
        assertTrue(allRowData.get(0).contains("id"));
        assertTrue(allRowData.get(0).contains("title"));
    }

}

@ReportGroup(name = "validReportDataSource")
class ValidReportDataSource {

    @Report
    public List<SampleData> sampleReport() {
        return asList(new SampleData("id"));
    }
}

@ReportGroup(name = "validPagedReportDataSource")
class ValidPagedReportDataSource {

    @Report
    public List<SampleData> sampleReport(int pageNumber) {
        if(pageNumber == 2)
            return null;
        return asList(new SampleData("id"));
    }
}


@ReportGroup(name = "withoutDataMethod")
class WithoutDataMethod {
}

@ReportGroup(name = "withPrivateDataMethod")
class WithPrivateDataMethod {

    @Report
    private List<SampleData> sampleReport(int pageNumber) {
        return asList(new SampleData("id"));
    }

}

@ReportGroup(name = "withInvalidDataMethod")
class WithInvalidDataMethod {

    @Report
    public List<SampleData> sampleReport() {
        return asList(new SampleData("id"));
    }

}

@ReportGroup(name = "withInvalidReturnType")
class WithInvalidReturnType {

    @Report
    public SampleData sampleReport() {
        return new SampleData("id");
    }
}

