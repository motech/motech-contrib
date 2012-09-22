package org.motechproject.timeseries.pipeline.contract;

public class PipeHeadDefinition implements PipeComponent {

    private FunctionDefinition function;

    private DataSource dataSource;

    @Override
    public boolean hasParameter(String parameterName) {
        if (parameterName.replace('.', ':').split(":")[0].equalsIgnoreCase("function")) {
            return function.hasParameter(parameterName.replace('.', ':').split(":")[1]);
        } else {
            return dataSource.hasParameter(parameterName.replace('.', ':').split(":")[1]);
        }
    }

    public FunctionDefinition getFunction() {
        return function;
    }

    public void setFunction(FunctionDefinition function) {
        this.function = function;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
