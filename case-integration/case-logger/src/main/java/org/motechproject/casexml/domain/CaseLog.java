package org.motechproject.casexml.domain;

import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@TypeDiscriminator("doc.type === 'CaseLog'")
public class CaseLog extends MotechBaseDataObject {

    private String contextPath;
    private String request;
    private boolean hasException;
    private String response;
    private DateTime logDate;

    public CaseLog() {
    }

    public CaseLog(String requestBody, String requestURI, boolean hasException, DateTime logDate) {
        request = requestBody;
        contextPath = requestURI;
        this.hasException = hasException;
        this.logDate = logDate;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
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
        return response;
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
}
