package org.motechproject.importer.domain;

import java.util.ArrayList;
import java.util.List;

public class ValidationResponse {
    private boolean isValid;
    private List<Error> errors;

    public ValidationResponse(boolean valid) {
        isValid = valid;
        errors = new ArrayList<Error>();
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return isValid;
    }
}

