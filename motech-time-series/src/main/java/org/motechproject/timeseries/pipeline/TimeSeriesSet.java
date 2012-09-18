package org.motechproject.timeseries.pipeline;


import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class TimeSeriesSet {

    private TimeSeriesRecord record;
    private List data;

    public TimeSeriesSet(TimeSeriesRecord record) {
        this.record = record;
        this.data = allValues(record.allDataPoints());
    }

    public TimeSeriesSet apply(PipeTransformation reader) {
        if (!data.isEmpty()) {
            if (data.get(0) instanceof List) {
                return applyTransformationOnEveryElement(reader);
            } else {
                return applyTransformation(reader);
            }
        } else {
            return this;
        }
    }

    private TimeSeriesSet applyTransformation(PipeTransformation reader) {
        Object result = reader.process(data);
        if (result instanceof List) {
            this.data = (List) result;
        } else {
            this.data = new ArrayList();
            this.data.add(result);
        }
        return this;
    }

    private TimeSeriesSet applyTransformationOnEveryElement(PipeTransformation reader) {
        List result = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            List<Double> element = (List<Double>) data.get(i);
            result.add(reader.process(element));
        }
        this.data = result;
        return this;
    }

    private List allValues(List<DataPoint> points) {
        List values = new ArrayList();
        for (DataPoint point : points) {
            values.add(point.getValue());
        }
        return values;
    }
}

