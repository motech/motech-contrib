package org.motechproject.export.service.sample;

import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@ExcelDataSource(name = "sampleExcel")
public class SampleExcelDataSource {

    private List<SampleData> sampleData1 = new ArrayList<SampleData>();
    private List<SampleData> sampleData2 = new ArrayList<SampleData>();
    public boolean isCalled = false;

    public SampleExcelDataSource() {
        this.sampleData1 = asList(new SampleData("id1"), new SampleData("id2"));
        this.sampleData2 = asList(new SampleData("id3"));
    }

    @DataProvider
    public List<SampleData> sampleExcel(int pageNumber) {
        isCalled = true;
        if (pageNumber == 1) {
            return sampleData1;
        } else if (pageNumber == 2) {
            return sampleData2;
        } else {
            return null;
        }
    }
}
