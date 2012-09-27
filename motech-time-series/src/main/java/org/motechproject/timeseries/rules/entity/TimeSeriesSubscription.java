package org.motechproject.timeseries.rules.entity;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'TimeSeriesSubscription'")
public class TimeSeriesSubscription extends MotechBaseDataObject {

    private String externalId;
    private String event;
    private String ruleName;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String pipeName) {
        this.ruleName = pipeName;
    }
}
