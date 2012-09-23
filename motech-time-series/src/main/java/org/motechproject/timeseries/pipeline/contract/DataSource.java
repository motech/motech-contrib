package org.motechproject.timeseries.pipeline.contract;

import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.domain.valueobject.DataPoint;
import org.motechproject.timeseries.domain.valueobject.DateRange;
import org.motechproject.timeseries.pipeline.TimeSeriesDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

public class DataSource implements PipeComponent {

    private String type;

    private Map<String, String> query = new HashMap<>();

    @Override
    public boolean hasParameter(String parameterName) {
        return query.containsKey(parameterName);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public void queryWith(String[][] params) {
        for (int i = 0; i < params.length; i++) {
            query.put(params[i][0], params[i][1]);
        }
    }

    public List<List<DataPoint>> extractData(TimeSeriesRecord record) {
        return asList(record.allDataPoints(parseInt(query.get("points"))));
    }

    public List<DataPoint> queryOn(TimeSeriesRecord record) {
        if ("points".equalsIgnoreCase(type)) {
            return record.allDataPoints(Integer.parseInt(query.get("number")));
        } else {
            TimeSeriesDate fromDate = new TimeSeriesDate(query.get("from"));
            TimeSeriesDate toDate = new TimeSeriesDate(query.get("to"));
            return record.allDataPointsMatching(asList(new DateRange(fromDate.getValue(), toDate.getValue())));
        }
    }
}
