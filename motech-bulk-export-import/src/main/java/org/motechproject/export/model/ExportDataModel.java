package org.motechproject.export.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExportDataModel {

    private final Columns columns;

    public ExportDataModel(Type returnType) {
        Type[] typeParameters = ((ParameterizedType) returnType).getActualTypeArguments();
        assertHasSingleTypeParameter(typeParameters);

        columns = new Columns((Class) typeParameters[0]);
    }

    private void assertHasSingleTypeParameter(Type[] typeParameters) {
        if (typeParameters.length != 1) {
            throw new RuntimeException("Return type should have only one generics type parameter");
        }
    }

    public List<String> columnHeaders() {
        List<String> columnHeaders = new ArrayList<String>();
        for (Column column : columns) {
            columnHeaders.add(column.name());
        }
        return columnHeaders;
    }

    public List<String> columnFormats() {
        List<String> columnFormats = new ArrayList<String>();
        for (Column column : columns) {
            columnFormats.add(column.format());
        }
        return columnFormats;
    }

    public List<Object> rowData(Object model) {
        List<Object> rowData = new ArrayList<Object>();
        for (Column column : columns) {
            rowData.add(column.value(model));
        }
        return rowData;
    }

}
