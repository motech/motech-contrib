package org.motechproject.importer.domain;

public class CSVImportResponse {
    String lastProcessedFileName;
    boolean isImportSuccessful;

    public CSVImportResponse(String lastProcessedFileName, boolean importSuccessful) {
        this.lastProcessedFileName = lastProcessedFileName;
        isImportSuccessful = importSuccessful;
    }

    public String getLastProcessedFileName() {
        return lastProcessedFileName;
    }

    public boolean isImportSuccessful() {
        return isImportSuccessful;
    }
}
