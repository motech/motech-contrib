package org.motechproject.casexml.service.response;

import org.motechproject.casexml.service.exception.CaseException;

import java.util.ArrayList;
import java.util.List;

public class CaseResponse {
    public List<CaseException> errors;
    public String status;

    public String message;

    public List<CaseException> getErrors() {
        return errors;
    }

    public void setErrors(List<CaseException> errors) {
        this.errors = errors;
    }

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

    public CaseResponse(){
        errors = new ArrayList<CaseException>();
    }

    public void add(CaseException error){
        errors.add(error);
    }
}
