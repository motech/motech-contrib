package org.motechproject.timeseries.pipeline.infrastructure;

import com.google.gson.JsonObject;

public class CronPipe extends Pipe {

    public CronPipe(JsonObject definition) {
        super(definition);
    }

    public String getCronExpression() {
        return definition.get("expression").getAsString();
    }
}
