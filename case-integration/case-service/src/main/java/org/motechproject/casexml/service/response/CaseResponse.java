package org.motechproject.casexml.service.response;

import org.motechproject.casexml.service.exception.CaseException;

public class CaseResponse {
    public CaseException getException() {
        return errors;
    }

    public void setException(CaseException exception) {
        this.errors = exception;
    }

    public CaseException errors;
    public String status;

    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
