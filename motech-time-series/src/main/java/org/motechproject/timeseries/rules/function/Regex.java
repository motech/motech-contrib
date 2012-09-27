package org.motechproject.timeseries.rules.function;

import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.TimeSeriesSet;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.matches;

public class Regex {

    private String regexp;

    public Regex(String regexp) {
        this.regexp = regexp;
    }

    public TimeSeriesSet process(TimeSeriesSet data) {
        TimeSeriesSet results = new TimeSeriesSet();
        for (List<DataPoint> row : data.getData()) {
            DataPoint result = new DataPoint();
            result.startFrom(row.get(0).getStartTime());
            result.extendTill(row.get(row.size() - 1).getEndTime());
            result.setValue(matches(regexp, asString(row)) ? 1d : 0d);
            results.addRow(asList(result));
        }
        return results;
    }

    private String asString(List<DataPoint> row) {
        StringBuilder result = new StringBuilder();
        for (DataPoint point : row) {
            result.append(point.getValue().toString());
        }
        return result.toString();
    }
}
