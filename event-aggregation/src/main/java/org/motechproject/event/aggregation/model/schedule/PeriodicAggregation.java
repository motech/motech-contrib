package org.motechproject.event.aggregation.model.schedule;

import org.joda.time.DateTime;
import org.joda.time.Period;

public interface PeriodicAggregation {

    Period getPeriod();
    DateTime getStartTime();
}
