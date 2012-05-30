package org.motechproject.importer.domain;

public class Error {
    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
