package org.motechproject.timeseries.domain.entity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.DateRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@TypeDiscriminator("doc.type == 'TimeSeriesRecord'")
public class TimeSeriesRecord extends MotechBaseDataObject {

    private String externalId;

    @JsonProperty
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

    public List<DataPoint> allDataPoints(int nos) {
        Vector<DataPoint> result = new Vector<>();
        for (int i = nos; i >= 0; i--) {
            if (i < dataPoints.size()) {
                result.insertElementAt(dataPoints.get(i), 0);
            }
        }
        return result;
    }

    public List<DataPoint> allDataPoints() {
        return new ArrayList<>(dataPoints);
    }
}
