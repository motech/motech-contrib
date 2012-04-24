package org.motechproject.casexml.service.response;

import org.motechproject.casexml.service.exception.CaseValidationException;

import java.util.ArrayList;
import java.util.List;

public class CaseResponse {
    public List<CaseValidationException> errors;
    public String status;

    public String message;

    public List<CaseValidationException> getErrors() {
        return errors;
    }

    public void setErrors(List<CaseValidationException> errors) {
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
        errors = new ArrayList<CaseValidationException>();
    }

    public void add(CaseValidationException error){
        errors.add(error);
    }
}
