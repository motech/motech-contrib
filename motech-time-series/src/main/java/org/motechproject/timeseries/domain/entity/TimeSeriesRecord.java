package org.motechproject.timeseries.domain.entity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.DateRange;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;

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

    public TimeSeriesSet allDataPointsMatching(List<DateRange> ranges) {
        TimeSeriesSet set = new TimeSeriesSet();
        for (DateRange range : ranges) {
            List<DataPoint> matchedPoints = new ArrayList<>();
            for (DataPoint point : dataPoints) {
                if (point.matches(range))
                    matchedPoints.add(point);
            }
            set.addRow(matchedPoints);
        }
        return set;
    }

    public TimeSeriesSet allDataPoints(int nos) {
        TimeSeriesSet set = new TimeSeriesSet();
        Vector<DataPoint> result = new Vector<>();

        for (int i = nos; i >= 0; i--) {
            if (i < dataPoints.size()) {
                result.insertElementAt(dataPoints.get(i), 0);
            }
        }
        set.addRow(result);
        return set;
    }

    public TimeSeriesSet allDataPoints(List<Integer> nos) {
        TimeSeriesSet set = new TimeSeriesSet();
        for (Integer no : nos) {
            Vector<DataPoint> result = new Vector<>();
            for (int i = no; i >= 0; i--) {
                if (i < dataPoints.size()) {
                    result.insertElementAt(dataPoints.get(i), 0);
                }
            }
            set.addRow(result);
        }
        return set;
    }

    public List<DataPoint> allDataPoints() {
        return new ArrayList<>(dataPoints);
    }
}
