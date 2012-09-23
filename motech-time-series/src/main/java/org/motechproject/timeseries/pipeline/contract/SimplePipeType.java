package org.motechproject.timeseries.pipeline.contract;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class SimplePipeType implements PipeComponent {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasParameter(String parameterName) {
        return "name".equalsIgnoreCase(parameterName);
    }
}
