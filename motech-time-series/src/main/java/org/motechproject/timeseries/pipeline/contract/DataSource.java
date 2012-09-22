package org.motechproject.timeseries.pipeline.contract;

import java.util.Map;

public abstract class DataSource implements PipeComponent {

    private String type;

    private Map<String, String> query;

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
}
