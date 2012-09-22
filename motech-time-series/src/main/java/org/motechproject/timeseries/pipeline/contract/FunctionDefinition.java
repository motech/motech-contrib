package org.motechproject.timeseries.pipeline.contract;

import java.util.Map;

public class FunctionDefinition implements PipeComponent {

    private String name;

    private Map<String, String> configuration;

    @Override
    public boolean hasParameter(String parameterName) {
        return configuration.containsKey(parameterName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
}
