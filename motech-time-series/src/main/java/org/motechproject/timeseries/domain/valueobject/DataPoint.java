package org.motechproject.timeseries.domain.valueobject;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class DataPoint {

    @JsonProperty
    private DateRange range;

    private Double value;

    public DataPoint() {
    }

    public DataPoint(DateTime from, DateTime to) {
        range = new DateRange(from, to);
    }

    public boolean matches(DateRange range) {
        return this.range.matches(range);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPoint point = (DataPoint) o;

        if (range != null ? !range.equals(point.range) : point.range != null) return false;
        if (value != null ? !value.equals(point.value) : point.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = range != null ? range.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
