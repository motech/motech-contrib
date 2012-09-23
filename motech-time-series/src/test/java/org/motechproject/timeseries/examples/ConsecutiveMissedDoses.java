package org.motechproject.timeseries.examples;

import org.motechproject.timeseries.pipeline.contract.*;

import java.util.List;

import static java.util.Arrays.asList;

public class ConsecutiveMissedDoses {

    public PipeLine build() {
        PipeLine pipeLine = new PipeLine();
        pipeLine.setName("consecutiveMissedDoses");
        pipeLine.setType(createType());
        pipeLine.setTransformations(createTransformations());
        return pipeLine;
    }

    private SimplePipeType createType() {
        SimplePipeType type = new SimplePipeType();
        type.setName("onRecordEvent");
        return type;
    }

    private PipeLineTransformations createTransformations() {
        PipeLineTransformations transformations = new PipeLineTransformations();
        transformations.setHead(createHeadDefinition());
        transformations.setLine(createLineDefinition());
        return transformations;
    }

    private PipeHeadDefinition createHeadDefinition() {
        PipeHeadDefinition headDefinition = new PipeHeadDefinition();
        headDefinition.setDataSource(createDataSource());
        headDefinition.setFunction(new FunctionDefinition().setName("group"));
        return headDefinition;
    }

    private List<FunctionDefinition> createLineDefinition() {
        return asList(
                new FunctionDefinition().setName("select").setConfiguration(new String[][]{{"value", "0"}}),
                new FunctionDefinition().setName("summation"),
                new FunctionDefinition().setName("threshold").setConfiguration(new String[][]{{"threshold", "2"}, {"lessThan", "false"}})
        );
    }

    private DataSource createDataSource() {
        DataSource source = new DataSource();
        source.setType("points");
        source.queryWith(new String[][]{{"number", "5"}});
        return source;
    }
}
