package org.motechproject.export.model;

import java.util.List;

public class ExportData {
    private List<String> columnHeaders;
    private List<String> columnFormats;
    private List<List<Object>> allRowData;

    public ExportData(List<String> columnHeaders, List<String> columnFormats, List<List<Object>> allRowData) {
        this.columnHeaders = columnHeaders;
        this.columnFormats = columnFormats;
        this.allRowData = allRowData;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public List<String> getColumnFormats() {
        return columnFormats;
    }

    public List<List<Object>> getAllRowData() {
        return allRowData;
    }
}
