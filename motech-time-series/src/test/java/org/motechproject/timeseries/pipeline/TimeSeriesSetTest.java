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
        public Object process(List<Double> data) {
            System.out.println("called");
            return data.get(0);
        }
    }

    private static class TransformationWhichReturnsMultipleElements implements PipeTransformation {


        /*TODO: This contract should be replaced by a type safe contract*/
        @Override
        public Object process(List<Double> data) {
            System.out.println("called multiple element return transformation");
            return asList(asList(data.get(0)), asList(data.get(0)));
        }
    }
}
