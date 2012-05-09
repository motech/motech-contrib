package org.motechproject.provider.registration.exception;

import org.motechproject.casexml.exception.CaseParserException;
import org.springframework.http.HttpStatus;

public class OpenRosaRegistrationParserException extends CaseParserException {
    
    private HttpStatus statusCode;

    public OpenRosaRegistrationParserException(Exception cause, HttpStatus statusCode) {
        super(cause.getMessage());
        this.statusCode = statusCode;
    }
    
    public OpenRosaRegistrationParserException(Exception cause, String message, HttpStatus statusCode) {
        super(cause,message);
        this.statusCode = statusCode;
    }
    
    public OpenRosaRegistrationParserException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

  }
