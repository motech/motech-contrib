package org.motechproject.timeseries.domain.valueobjects;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class DateRange {

    @JsonProperty
    private DateTime from;
    @JsonProperty
    private DateTime to;

    public DateRange() {
    }

    public DateRange(DateTime from, DateTime to) {
        this.from = from;
        this.to = to;
    }

    public boolean matches(DateRange range) {
        return isWithin(range.from) || isWithin(range.to) || encloses(range);
    }

    private boolean isWithin(DateTime time) {
        return isOnOrAfter(from, time) && isOnOrBefore(to, time);
    }

    private boolean encloses(DateRange reference) {
        return isOnOrBefore(from, reference.from) && isOnOrAfter(to, reference.to);
    }

    private boolean isOnOrBefore(DateTime reference, DateTime compared) {
        return compared.isEqual(reference) || compared.isBefore(reference);
    }

    private boolean isOnOrAfter(DateTime reference, DateTime compared) {
        return compared.isEqual(reference) || compared.isAfter(reference);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateRange range = (DateRange) o;

        if (from != null ? !from.isEqual(range.from) : range.from != null) return false;
        if (to != null ? !to.isEqual(range.to) : range.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
