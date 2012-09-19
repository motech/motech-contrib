package org.motechproject.timeseries.pipeline;

import org.junit.Test;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.pipeline.transformation.*;

import static java.util.Arrays.asList;

/*TODO: This construction mechanism will replaced by pipes obtained from stores*/
public class PipeLineTest {

    @Test
    public void simpleThresholdTest() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(1d), new DataPoint(1d), new DataPoint(0d), new DataPoint(1d)));

        new PipeLine(asList(new Summarizer(), new Thresholder(5d))).process(new TimeSeriesSet(record));
    }

    @Test
    public void consecutiveMissedDosesTest() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(0d)));

        new PipeLine(asList(new Grouper(), new Masker(0d), new Summarizer(), new Thresholder(2d, false))).process(new TimeSeriesSet(record));
    }

    @Test
    public void missedDoseEveryOtherDay() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(1d), new DataPoint(0d), new DataPoint(1d), new DataPoint(0d)));

        new PipeLine(asList(new PatternMatcher("(0.01.0).+"), new Thresholder(0d, false))).process(new TimeSeriesSet(record));
    }

    /*Shows the 2d to 1d transform*/
    @Test
    public void missedExactlyTwoDosesWithAnIntervalOfExactlyTwoDays() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(1d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d)));

        new PipeLine(asList(new Clusterer(0d), new PatternMatcher("0.00.0"), new PatternMatcher(".*1.00.00.01.0.*"), new Thresholder(0d, false))).process(new TimeSeriesSet(record));
    }

    @Test
    public void missedAtleastTwoDosesWithAnIntervalOfExactlyTwoDays() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(1d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d)));

        new PipeLine(asList(new Clusterer(0d), new Masker(0d), new Summarizer(), new Thresholder(1.0, false), new PatternMatcher(".*1.00.00.01.0.*"), new Thresholder(0d, false))).process(new TimeSeriesSet(record));
    }

    @Test
    public void missedAtleastTwoDosesWithAnIntervalOfAtleastTwoDays() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(0d), new DataPoint(0d), new DataPoint(1d), new DataPoint(1d), new DataPoint(1d), new DataPoint(0d), new DataPoint(0d), new DataPoint(1d)));

        new PipeLine(asList(new Clusterer(0d), new Masker(0d), new Summarizer(), new Thresholder(1.0, false), new PatternMatcher(".*1.00.00.0(0.0).*1.0.*"), new Thresholder(0d, false))).process(new TimeSeriesSet(record));
    }
}
