package org.motechproject.timeseries.domain;


import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.DateRange;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TimeSeriesRecordTest {

    private DateTime today = DateTime.now();

    @Test
    public void shouldReturnAllDataPointsWithinRange() {
        TimeSeriesRecord record = new TimeSeriesRecord();
        DataPoint expectedDataPoint = new DataPoint(daysFromToday(1), daysFromToday(2));
        record.addDataPoints(
                asList(
                        expectedDataPoint,
                        new DataPoint(daysFromToday(4), daysFromToday(5))
                )
        );
        assertEquals(expectedDataPoint, record.allDataPointsMatching(asList(new DateRange(today, daysFromToday(3)))).get(0));
    }

    private DateTime daysFromToday(int days) {
        return today.plusDays(days);
    }
}
