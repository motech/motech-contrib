package org.motechproject.export.controller.sample;

import org.motechproject.export.annotation.ExcelReportGroup;
import org.motechproject.export.annotation.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@ExcelReportGroup(name = "sampleExcelReports")
public class SampleExcelReportController {

    private List<SampleData> sampleData1 = new ArrayList<SampleData>();
    private List<SampleData> sampleData2 = new ArrayList<SampleData>();
    public boolean isCalled = false;

    public SampleExcelReportController() {
        this.sampleData1 = asList(new SampleData("id1"), new SampleData("id2"));
        this.sampleData2 = asList(new SampleData("id3"));
    }

    @Report
    public List<SampleData> sampleExcelReports(int pageNumber) {
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
