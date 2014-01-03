package org.motechproject.importer.domain;

import java.util.ArrayList;
import java.util.List;

public class ValidationResponse {
    private boolean isValid;
    private List<Error> errors;
    private List<Object> invalidRecords;

    public ValidationResponse(boolean valid) {
        isValid = valid;
        errors = new ArrayList<>();
        invalidRecords = new ArrayList<>();
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public void addErrors(List<Error> errors) {
        this.errors.addAll(errors);
    }

    public void addInvalidRecord(Object invalidRecord) {
        this.invalidRecords.add(invalidRecord);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public List<Object> getInvalidRecords() {
        return invalidRecords;
    }

    public boolean isValid() {
        return isValid;
    }
}

