package org.motechproject.timeseries.pipeline.contract;

import java.util.List;

public class PipeLineTransformations implements PipeComponent {

    PipeHeadDefinition head;

    List<FunctionDefinition> line;

    @Override
    public boolean hasParameter(String parameterName) {
        if (parameterName.replace('.', ':').split(":")[0].equalsIgnoreCase("head")) {
            return head.hasParameter(parameterName.replace('.', ':').split(":")[1]);
        }
        return false;
    }

    public PipeHeadDefinition getHead() {
        return head;
    }

    public void setHead(PipeHeadDefinition head) {
        this.head = head;
    }

    public List<FunctionDefinition> getLine() {
        return line;
    }

    public void setLine(List<FunctionDefinition> line) {
        this.line = line;
    }
}
