package org.motechproject.timeseries.pipeline.contract;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;

public class PipeLine implements PipeComponent {

    private String name;

    private SimplePipeType type;

    private PipeLineTransformations transformations;

    private LocalDate validFrom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimplePipeType getType() {
        return type;
    }

    public void setType(SimplePipeType type) {
        this.type = type;
    }

    public PipeLineTransformations getTransformations() {
        return transformations;
    }

    public void setTransformations(PipeLineTransformations transformations) {
        this.transformations = transformations;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public boolean hasParameter(String name) {
        String firstComponant = name.replace(".", ":").split(":")[0];
        if ("type".equalsIgnoreCase(firstComponant)) {
            return type.hasParameter(name.substring(name.indexOf(".") + 1));
        }
        return false;
    }

    public boolean baselineValidity(LocalDate date) {
        if (null == validFrom) {
            validFrom = date;
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    public boolean isValid() {
        return null != validFrom;
    }
}
