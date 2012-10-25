package org.motechproject.export.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExportDataModel {

    private final Columns columns;

    public ExportDataModel(Type returnType) {
        columns = new Columns((Class) returnType);
    }

    public List<String> columnHeaders() {
        List<String> columnHeaders = new ArrayList<>();
        for (Column column : columns) {
            columnHeaders.add(column.name());
        }
        return columnHeaders;
    }

    public List<String> rowData(Object model) {
        List<String> rowData = new ArrayList<>();
        for (Column column : columns) {
            rowData.add(column.value(model));
        }
        return rowData;
    }

}
