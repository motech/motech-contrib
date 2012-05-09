package org.motechproject.provider.registration.exception;

import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

public class OpenRosaRegistrationValidationException extends CaseException {


    public OpenRosaRegistrationValidationException(Exception cause, HttpStatus statusCode) {
        super(cause,statusCode);
    }

    public OpenRosaRegistrationValidationException(Exception cause, String message, HttpStatus statusCode) {
        super(cause,message,statusCode);
    }

    public OpenRosaRegistrationValidationException(String message, HttpStatus statusCode) {
        super(message,statusCode);
    }
}
