package org.motechproject.timeseries.pipeline;

import org.junit.Test;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TimeSeriesSetTest {

    @Test
    public void testListWithSingleElements() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(), new DataPoint()));

        TimeSeriesSet seriesSet = new TimeSeriesSet(record);
        seriesSet.apply(new Transformation(), Collections.<String, String>emptyMap());
    }

    @Test
    public void testListWithListOfElements() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        record.addDataPoints(asList(new DataPoint(), new DataPoint()));

        TimeSeriesSet seriesSet = new TimeSeriesSet(record);
        seriesSet.apply(new TransformationWhichReturnsMultipleElements(),  Collections.<String, String>emptyMap()).apply(new Transformation(),  Collections.<String, String>emptyMap());
    }

    private static class Transformation implements PipeTransformation {

        @Override
        public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
            System.out.println("called");
            return data;
        }

        @Override
        public String name() {
            return "transformation";
        }


    }

    private static class TransformationWhichReturnsMultipleElements implements PipeTransformation {


        @Override
        public List<List<DataPoint>> process(List<List<DataPoint>> data, Map<String, String> configuration) {
            System.out.println("called multiple element return transformation");
            return asList(data.get(0), data.get(0));
        }

        @Override
        public String name() {
            return "transformationWhichReturnsMultipleElements";
        }
    }
}
