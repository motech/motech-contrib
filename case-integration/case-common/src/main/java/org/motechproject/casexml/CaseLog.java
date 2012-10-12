package org.motechproject.casexml;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'CaseLog'")
public class CaseLog extends MotechBaseDataObject {

    private String contextPath;
    private String request;
    private boolean hasException;

    public CaseLog() {
    }

    public CaseLog(String requestBody, String requestURI, boolean hasException) {
        request = requestBody;
        contextPath = requestURI;
        this.hasException = hasException;
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
}
