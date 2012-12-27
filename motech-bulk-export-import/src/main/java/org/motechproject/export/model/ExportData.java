package org.motechproject.export.model;

import java.util.List;

public class ExportData {

    private List<String> columnHeaders;
    private List<List<Object>> allRowData;

    public ExportData(List<String> columnHeaders, List<List<Object>> allRowData) {
        this.columnHeaders = columnHeaders;
        this.allRowData = allRowData;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public List<List<Object>> getAllRowData() {
        return allRowData;
    }
}
