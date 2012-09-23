package org.motechproject.timeseries.pipeline.contract;

import java.util.List;

public class PipeHeadDefinition implements PipeComponent {

    private FunctionDefinition function;

    private List<DataSource> dataSources;

    @Override
    public boolean hasParameter(String parameterName) {
        if (parameterName.replace('.', ':').split(":")[0].equalsIgnoreCase("function")) {
            return function.hasParameter(parameterName.replace('.', ':').split(":")[1]);
        }
        return false;
    }

    public FunctionDefinition getFunction() {
        return function;
    }

    public void setFunction(FunctionDefinition function) {
        this.function = function;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }
}
