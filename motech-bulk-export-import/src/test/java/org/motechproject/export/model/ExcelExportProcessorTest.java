package org.motechproject.export.model;

import org.junit.Test;
import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;
import org.motechproject.export.service.sample.SampleData;
import org.motechproject.export.service.sample.SampleExcelDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

public class ExcelExportProcessorTest {

    @Test
    public void isValidDataSourceIfAnnotatedWithReport() {
        assertTrue(ExcelExportProcessor.isValidDataSource(SampleExcelDataSource.class));
    }

    @Test
    public void isNotValidDataSourceIfNotAnnotatedWithReport() {
        assertFalse(ExcelExportProcessor.isValidDataSource(ExcelExportProcessorTest.class));
    }

    @Test
    public void nameIsSpecifiedInReportAnnotation() {
        assertEquals("sampleExcel", new ExcelExportProcessor(new SampleExcelDataSource()).name());
    }

    @Test
    public void shouldRetrieveDataFromDataSource() {
        assertArrayEquals(
                new SampleData[]{new SampleData("id1"), new SampleData("id2")},
                new ExcelExportProcessor(new SampleExcelDataSource()).dataForPage("sampleExcel", 1).toArray()
        );
    }

    @Test
    public void shouldRetrieveEmptyListWhenDataMethodIsNotSpecified() {
        assertEquals(0, new ExcelExportProcessor(new WithoutDataMethod()).dataForPage("sampleExcel", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodSpecifiedIsNotPublic() {
        assertEquals(0, new ExcelExportProcessor(new WithPrivateDataMethod()).dataForPage("sampleExcel", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotHavePageNumber() {
        assertEquals(0, new ExcelExportProcessor(new WithInvalidDataMethod()).dataForPage("sampleExcel", 1).size());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenDataMethodDoesNotReturnAList() {
        assertEquals(0, new ExcelExportProcessor(new WithInvalidReturnType()).dataForPage("sampleExcel", 1).size());
    }

    @Test
    public void shouldReturnNameForTitle() {
        assertEquals("Without Data Method", new ExcelExportProcessor(new WithoutDataMethod()).title());
    }

    @Test
    public void shouldReturnColumnHeaders() {
        assertEquals(asList("Id", "Custom column name", "Boolean Value"), new ExcelExportProcessor(new ValidReportDataSource()).columnHeaders("sampleExcel"));
    }

    @Test
    public void shouldCreateAnEntireReportWithHeadersAndRows() {
        ExcelExportProcessor excelExportProcessor = new ExcelExportProcessor(new ValidReportDataSource());
        Map<String, String> criteria = new HashMap<String, String>();
        ExportData export = excelExportProcessor.createEntireReport("sampleExcel", criteria);

        List<String> columnHeaders = export.getColumnHeaders();
        List<List<String>> allRowData = export.getAllRowData();

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
        ExcelExportProcessor excelExportProcessor = new ExcelExportProcessor(new ValidPagedReportDataSource());
        ExportData export = excelExportProcessor.createPagedReport("sampleExcel");

        List<String> columnHeaders = export.getColumnHeaders();
        List<List<String>> allRowData = export.getAllRowData();

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

@ExcelDataSource(name = "validReportDataSource")
class ValidReportDataSource {

    @DataProvider
    public List<SampleData> sampleExcel(Map<String, String> criteria) {
        return asList(new SampleData("id"));
    }
}

@ExcelDataSource(name = "validPagedReportDataSource")
class ValidPagedReportDataSource {

    @DataProvider
    public List<SampleData> sampleExcel(int pageNumber) {
        if (pageNumber == 2)
            return null;
        return asList(new SampleData("id"));
    }
}


@ExcelDataSource(name = "withoutDataMethod")
class WithoutDataMethod {
}

@ExcelDataSource(name = "withPrivateDataMethod")
class WithPrivateDataMethod {

    @DataProvider
    private List<SampleData> sampleExcel(int pageNumber) {
        return asList(new SampleData("id"));
    }

}

@ExcelDataSource(name = "withInvalidDataMethod")
class WithInvalidDataMethod {

    @DataProvider
    public List<SampleData> sampleExcel() {
        return asList(new SampleData("id"));
    }

}

@ExcelDataSource(name = "withInvalidReturnType")
class WithInvalidReturnType {

    @DataProvider
    public SampleData sampleExcel() {
        return new SampleData("id");
    }
}

