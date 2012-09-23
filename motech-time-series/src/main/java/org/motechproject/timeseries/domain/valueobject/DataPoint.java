package org.motechproject.timeseries.domain.valueobject;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class DataPoint {

    @JsonProperty
    private DateRange range;

    private Double value = 0d;

    @JsonProperty
    private Double ideal = 1d;

    public DataPoint() {
        range = new DateRange();
    }

    public DataPoint(DateTime from, DateTime to) {
        this();
        range = new DateRange(from, to);
    }

    public DataPoint(Double value) {
        this();
        this.value = value;
    }

    public DataPoint(DataPoint point) {
        this();
        range = point.range;
        value = point.getValue();
        ideal = point.getIdeal();
    }

    public DataPoint(double value, DateTime dateTime) {
        this.value = value;
        this.range = new DateRange(dateTime, dateTime);
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

    public void setRange(DateRange range) {
        this.range = range;
    }

    public Double getIdeal() {
        return ideal;
    }

    public void setIdeal(Double ideal) {
        this.ideal = ideal;
    }

    public void startFrom(DateTime time) {
        if (range.hasNotStarted()) {
            range.setStartDate(time);
        }
    }

    public void extendTill(DateTime time) {
        range.setEndDate(time);
    }

    @JsonIgnore
    public DateTime getStartTime() {
        return range.getStartTime();
    }

    @JsonIgnore
    public DateTime getEndTime() {
        return range.getEndTime();
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
