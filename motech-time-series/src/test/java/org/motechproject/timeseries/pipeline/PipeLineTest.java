package org.motechproject.timeseries.pipeline;

import org.junit.Test;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.transformation.Counter;
import org.motechproject.timeseries.pipeline.transformation.Grouper;
import org.motechproject.timeseries.pipeline.transformation.Summarizer;
import org.motechproject.timeseries.pipeline.transformation.Thresholder;

import static java.util.Arrays.asList;

public class PipeLineTest {

    @Test
    public void simpleThresholdTest() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(1d), new DataPoint(1d), new DataPoint(0d), new DataPoint(1d)));

        /*TODO: This construction mechanism will replaced by pipes obtained from stores*/
        new PipeLine(asList(new Summarizer(), new Thresholder(5d))).process(new TimeSeriesSet(record));
    }

    @Test
    public void consecutiveMissedDosesTest() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(0d)));

        /*TODO: This construction mechanism will replaced by pipes obtained from stores*/
        new PipeLine(asList(new Grouper(), new Counter(0d), new Summarizer(), new Thresholder(2d, false))).process(new TimeSeriesSet(record));
    }
}
