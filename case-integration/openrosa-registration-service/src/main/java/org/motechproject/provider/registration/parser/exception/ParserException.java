package org.motechproject.provider.registration.parser.exception;

public class ParserException extends RuntimeException {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(Exception ex, String message) {
        super(message, ex);
    }
}
