package org.motechproject.timeseries.pipeline.service;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.timeseries.pipeline.contract.PipeLine;

@TypeDiscriminator("document.type == 'PipeLineRegistration'")
public class PipeLineRegistration extends MotechBaseDataObject {

    @JsonProperty
    private String externalId;

    @JsonProperty
    private PipeLine pipeLine;

    public PipeLineRegistration(String externalId, PipeLine pipeLine) {
        this.externalId = externalId;
        this.pipeLine = pipeLine;
    }

    public PipeLine getPipeLine() {
        return pipeLine;
    }
}
