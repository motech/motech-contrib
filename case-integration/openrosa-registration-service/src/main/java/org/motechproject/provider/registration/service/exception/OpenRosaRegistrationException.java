package org.motechproject.provider.registration.service.exception;

import org.springframework.http.HttpStatus;

public class OpenRosaRegistrationException extends Exception {
    
    private Exception innerException;
    private HttpStatus statusCode;
    private String message;

    public OpenRosaRegistrationException(Exception innerException, HttpStatus statusCode) {
        this.innerException = innerException;
        message = innerException.getMessage();
        this.statusCode = statusCode;
    }
    
    public OpenRosaRegistrationException(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public Exception getInnerException() {
        return innerException;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
