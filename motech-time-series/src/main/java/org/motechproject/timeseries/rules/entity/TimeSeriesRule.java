package org.motechproject.timeseries.rules.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.io.InputStream;

@TypeDiscriminator("doc.type === 'TimeSeriesRule'")
public class TimeSeriesRule extends MotechBaseDataObject {

    private String name;

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
}
