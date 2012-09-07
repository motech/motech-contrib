package org.motechproject.timeseries.pipeline.data;


import com.google.gson.JsonObject;

public class RageDataSource {

    private JsonObject definition;

    public RageDataSource(JsonObject definition) {
        this.definition = definition;
    }
}
