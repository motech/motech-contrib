package org.motechproject.casexml.service.exception;


import org.springframework.http.HttpStatus;

public class CaseValidationException extends Exception {

    private HttpStatus statusCode;
    private String message;

    public CaseValidationException(Exception cause, HttpStatus statusCode) {
        super(cause);
        this.message = cause.getMessage();
        this.statusCode = statusCode;
    }

    public CaseValidationException(Exception cause, String message, HttpStatus statusCode) {
        super(message, cause);
        this.message = message;
        this.statusCode = statusCode;
    }

    public CaseValidationException(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}