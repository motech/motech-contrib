package org.motechproject.timeseries.pipeline.infrastructure;


import com.google.gson.JsonObject;

public class Pipe {

    protected JsonObject definition;

    public Pipe(JsonObject definition) {
        this.definition = definition;
    }

    public HeadDefinition getHeadDefinition() {
        return new HeadDefinition(definition.get("head").getAsJsonObject());
    }

    public LineDefinition getLineDefinition() {
        return new LineDefinition(definition.get("head").getAsJsonArray());
    }
}
