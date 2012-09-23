package org.motechproject.timeseries.pipeline.contract;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class FunctionDefinition implements PipeComponent {

    private String name;

    @JsonProperty
    private Map<String, String> configuration = new HashMap<>();

    @Override
    public boolean hasParameter(String parameterName) {
        return configuration.containsKey(parameterName);
    }

    public String getName() {
        return name;
    }

    public FunctionDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public FunctionDefinition addConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
        return this;
    }

    @JsonIgnore
    public FunctionDefinition setConfiguration(String[][] configuration) {
        for (int i = 0; i < configuration.length; i++) {
            this.configuration.put(configuration[i][0], configuration[i][1]);
        }
        return this;
    }
}
