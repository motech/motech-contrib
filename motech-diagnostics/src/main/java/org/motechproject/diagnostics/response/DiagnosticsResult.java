package org.motechproject.diagnostics.response;

public class DiagnosticsResult {
    private boolean status;
    private String message;

    public DiagnosticsResult(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
