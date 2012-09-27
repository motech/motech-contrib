package org.motechproject.timeseries.rules.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.io.InputStream;

@TypeDiscriminator("doc.type === 'TimeSeriesRule'")
public class TimeSeriesRule extends MotechBaseDataObject {

    private String name;

    private String externalId;

    private String trigger;

    private InputStream inputStream;

    @JsonIgnore
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @JsonIgnore
    public String getContentType() {
        return "text/plain";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }
}
