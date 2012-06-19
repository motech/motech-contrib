package org.motechproject.export.model;

import java.util.List;

public class ExportData {

    private List<String> columnHeaders;
    private List<List<String>> allRowData;

    public ExportData(List<String> columnHeaders, List<List<String>> allRowData) {

        this.columnHeaders = columnHeaders;
        this.allRowData = allRowData;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public List<List<String>> getAllRowData() {
        return allRowData;
    }
}
