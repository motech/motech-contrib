package org.motechproject.export.writer;

import org.motechproject.export.model.CSVExportProcessor;
import org.motechproject.export.model.ExportData;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static au.com.bytecode.opencsv.CSVWriter.DEFAULT_SEPARATOR;
import static au.com.bytecode.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

@Component
public class CSVWriter {

    public void writeCSVData(Writer writer, CSVExportProcessor csvExportProcessor) {
        ExportData exportData = csvExportProcessor.getCSVData();
        au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(writer, DEFAULT_SEPARATOR, NO_QUOTE_CHARACTER);
        csvWriter.writeNext(exportData.getColumnHeaders().toArray(new String[]{}));
        csvWriter.writeAll(getData(exportData.getAllRowData()));
    }

    private List<String[]> getData(List<List<Object>> allRowData) {
        List<String[]> rows = new ArrayList<>();
        for (List<Object> row : allRowData) {
            String [] colValues = new String[row.size()];
            setColumnValues(row, colValues);
            rows.add(colValues);
        }
        return rows;
    }

    private void setColumnValues(List<Object> row, String[] colValues) {
        int i=0;
        for(Object columnValue : row){
            colValues[i++] = columnValue != null ? columnValue.toString() : null;
        }
    }
}
