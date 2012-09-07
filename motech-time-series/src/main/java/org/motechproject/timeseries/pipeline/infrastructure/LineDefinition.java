package org.motechproject.timeseries.pipeline.infrastructure;

import com.google.gson.JsonArray;

public class LineDefinition {

    private JsonArray definition;

    public LineDefinition(JsonArray definition) {
        this.definition = definition;
    }
}
