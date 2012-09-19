package org.motechproject.timeseries.pipeline;

import org.junit.Test;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;

import java.util.List;

import static java.util.Arrays.asList;

public class TimeSeriesSetTest {

    @Test
    public void testListWithSingleElements() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(), new DataPoint()));

        TimeSeriesSet seriesSet = new TimeSeriesSet(record);
        seriesSet.apply(new Transformation());
    }

    @Test
    public void testListWithListOfElements() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(), new DataPoint()));

        TimeSeriesSet seriesSet = new TimeSeriesSet(record);
        seriesSet.apply(new TransformationWhichReturnsMultipleElements()).apply(new Transformation());
    }

    private static class Transformation implements PipeTransformation {

        @Override
        public List<List<DataPoint>> process(List<List<DataPoint>> data) {
            System.out.println("called");
            return data;
        }
    }

    private static class TransformationWhichReturnsMultipleElements implements PipeTransformation {


        @Override
        public List<List<DataPoint>> process(List<List<DataPoint>> data) {
            System.out.println("called multiple element return transformation");
            return asList(data.get(0), data.get(0));
        }
    }
}
