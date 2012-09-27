package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Cluster {

    private Double valueToCluster;

    public Cluster(Double valueToCluster) {
        this.valueToCluster = valueToCluster;
    }

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet result = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            for (List<DataPoint> resultRow : cluster(row)) {
                result.addRow(resultRow);
            }
        }
        return result;
    }

    private List<List<DataPoint>> cluster(List<DataPoint> row) {
        List<List<DataPoint>> result = new ArrayList<>();
        List<DataPoint> currentSet = new ArrayList<>();

        for (DataPoint point : row) {
            if (point.getValue().equals(valueToCluster)) {
                currentSet.add(point);
            } else {
                if (!currentSet.isEmpty()) {
                    result.add(new ArrayList<>(currentSet));
                    currentSet.removeAll(currentSet);
                }
                result.add(asList(point));
            }
        }

        if (!currentSet.isEmpty())
            result.add(new ArrayList<>(currentSet));

        return result;
    }
}
