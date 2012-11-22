package org.motechproject.casexml.domain;

import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.util.StringUtil;

@TypeDiscriminator("doc.type === 'CaseLog'")
public class CaseLog extends MotechBaseDataObject {

    private String endpoint;
    private String entityId;
    private String requestType;
    private String request;
    private boolean hasException;
    private String response;
    private DateTime logDate;

    public CaseLog() {
    }

    public CaseLog(String entityId, String requestType, String requestBody, String requestURI, boolean hasException, DateTime logDate) {
        this.entityId = entityId;
        this.requestType = requestType;
        request = requestBody;
        endpoint = requestURI;
        this.hasException = hasException;
        this.logDate = logDate;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean getHasException() {
        return hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public String getResponse() {
        return StringUtil.isNullOrEmpty(response) ? "Null" : response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public DateTime getLogDate() {
        return logDate;
    }

    public void setLogDate(DateTime logDate) {
        this.logDate = DateUtil.setTimeZone(logDate);
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
