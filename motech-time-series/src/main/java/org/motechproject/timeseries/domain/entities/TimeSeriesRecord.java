package org.motechproject.timeseries.domain.entities;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.timeseries.domain.valueobjects.DataPoint;
import org.motechproject.timeseries.domain.valueobjects.DateRange;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'TimeSeriesRecord'")
public class TimeSeriesRecord extends MotechBaseDataObject {

    private String externalId;

    private List<DataPoint> dataPoints;

    public TimeSeriesRecord() {
        dataPoints = new ArrayList<>();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void addDataPoints(List<DataPoint> dataPoints) {
        for (DataPoint point : dataPoints)
            this.dataPoints.add(point);
    }

    public List<DataPoint> allDataPointsMatching(List<DateRange> ranges) {
        List<DataPoint> matchedPoints = new ArrayList<>();
        for (DateRange range : ranges) {
            for (DataPoint point : dataPoints) {
                if (point.matches(range))
                    matchedPoints.add(point);
            }
        }
        return matchedPoints;
    }
}
