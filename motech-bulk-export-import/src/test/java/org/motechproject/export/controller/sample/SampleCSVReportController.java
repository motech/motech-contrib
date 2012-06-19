package org.motechproject.export.controller.sample;

import org.motechproject.export.annotation.CSVReportGroup;
import org.motechproject.export.annotation.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@CSVReportGroup(name = "sampleCSVReports")
public class SampleCSVReportController {

    private List<SampleData> sampleData = new ArrayList<SampleData>();
    public boolean isCalled = false;

    public SampleCSVReportController() {
        this.sampleData = asList(new SampleData("id1"), new SampleData("id2"), new SampleData("id3"));
    }

    @Report
    public List<SampleData> sampleReport() {
        isCalled = true;
        return sampleData;
    }
}
