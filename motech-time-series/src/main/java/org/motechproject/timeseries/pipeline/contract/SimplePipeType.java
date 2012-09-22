package org.motechproject.timeseries.pipeline.contract;

public abstract class SimplePipeType implements PipeComponent {

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
