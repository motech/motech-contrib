package org.motechproject.export.service.sample;

import org.motechproject.export.annotation.CSVDataSource;
import org.motechproject.export.annotation.DataProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@CSVDataSource(name = "sampleCSVWithParameter")
public class SampleCSVDataSourceWithParameter {

    private List<SampleData> sampleData = new ArrayList<SampleData>();
    public boolean isCalled = false;
    public Object parameters;

    public SampleCSVDataSourceWithParameter() {
        this.sampleData = asList(new SampleData("id1"), new SampleData("id2"), new SampleData("id3"));
    }

    @DataProvider
    public List<SampleData> sampleCSV(Object parameters) {
        this.parameters = parameters;
        isCalled = true;
        return sampleData;
    }
}
