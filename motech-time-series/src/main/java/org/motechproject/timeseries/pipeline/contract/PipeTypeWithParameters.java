package org.motechproject.timeseries.pipeline.contract;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class PipeTypeWithParameters extends SimplePipeType {

    @JsonProperty
    private Map<String, String> configuration = new HashMap<>();

    public void addParameter(String name, String value) {
        configuration.put(name, value);
    }

    @Override
    public boolean hasParameter(String parameterName) {
        return super.hasParameter(parameterName) ? true : configuration.containsKey(parameterName);
    }
}
