package org.motechproject.timeseries.pipeline.infrastructure;


import com.google.gson.JsonObject;
import org.motechproject.timeseries.pipeline.data.DataSource;

import java.util.Collections;
import java.util.List;

public class HeadDefinition {

    private JsonObject definition;

    public HeadDefinition(JsonObject definition) {
        this.definition = definition;
    }

    public List<DataSource> getDataSources() {
        if (isMultiSourced()) {
            return getDataSources();
        }
        return Collections.emptyList();
    }

    private boolean isMultiSourced() {
        return definition.has("multiSource");
    }

}
