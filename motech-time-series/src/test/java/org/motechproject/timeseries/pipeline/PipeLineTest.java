package org.motechproject.timeseries.pipeline;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.timeseries.domain.collection.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.service.TimeSeriesService;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.examples.ConsecutiveMissedDoses;
import org.motechproject.timeseries.pipeline.repository.AllPipeLines;
import org.motechproject.timeseries.pipeline.service.TimeSeriesOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testTimeSeriesContext.xml")
public class PipeLineTest {

    @Autowired
    TimeSeriesOperationService operationService;

    @Autowired
    TimeSeriesService timeSeriesService;

    @Autowired
    AllTimeSeriesRecords records;

    @Autowired
    AllPipeLines pipeLines;


    @Test
    public void consecutiveMissedDosesTest() {
        operationService.registerForPipeLine("externalId", new ConsecutiveMissedDoses().build());
        TimeSeriesRecord record = new TimeSeriesRecord();

        record.setExternalId("externalId");
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(5d)));
        timeSeriesService.capture(record);
    }

    @After
    public void tearDown() {
        records.removeAll();
        pipeLines.removeAll();
    }

}
