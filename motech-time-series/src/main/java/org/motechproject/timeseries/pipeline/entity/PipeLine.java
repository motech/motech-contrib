package org.motechproject.timeseries.pipeline.entity;

import com.google.gson.JsonObject;
import org.motechproject.timeseries.pipeline.PipeLineException;
import org.motechproject.timeseries.pipeline.infrastructure.CronPipe;
import org.motechproject.timeseries.pipeline.infrastructure.Pipe;

public class PipeLine {

    private JsonObject definition;

    public String getName() {
        return definition.get("name").toString();
    }

    public CronPipe getCronPipe() {
        return readPipe("cron", CronPipe.class);
    }

    public Pipe getRecordEventPipe() {
        return readPipe("onRecordEvent", Pipe.class);
    }

    private <T extends Pipe> T readPipe(String event, Class<T> pipeClass) {
        if (definition.has(event)) {
            try {
                return pipeClass.getConstructor(JsonObject.class).newInstance(definition.get(event).getAsJsonObject());
            } catch (Exception e) {
                throw new PipeLineException(e);
            }
        } else {
            return null;
        }
    }
}
