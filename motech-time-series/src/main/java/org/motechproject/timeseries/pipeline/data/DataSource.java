package org.motechproject.timeseries.pipeline.data;

import com.google.gson.JsonObject;

public class DataSource {

    private JsonObject definition;

    public DataSourceType getDataSourceType() {
        if (definition.has("points")) {
            return DataSourceType.point;
        } else {
            return DataSourceType.range;
        }
    }

    public String getNumberOfPoints() {
        if (getDataSourceType().equals(DataSourceType.point)) {
            return definition.get("points").getAsString();
        } else {
            return "0";
        }
    }

    public RageDataSource getDateSourceDefinition() {
        if (getDataSourceType().equals(DataSourceType.range)) {
            return new RageDataSource(definition.get("dateRange").getAsJsonObject());
        }else{
            return null;
        }
    }
}
