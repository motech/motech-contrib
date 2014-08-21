package org.motechproject.event.aggregation.model.schedule.domain;

import org.motechproject.event.aggregation.model.schedule.AggregationSchedule;
import org.motechproject.mds.annotations.Entity;

import java.io.Serializable;

@Entity
public class AggregationScheduleRecord implements AggregationSchedule, Serializable {

    protected AggregationScheduleRecord() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregationScheduleRecord)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
