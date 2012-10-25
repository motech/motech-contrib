package org.motechproject.export.writer;

import org.motechproject.export.model.CSVExportProcessor;
import org.motechproject.export.model.ExportData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static au.com.bytecode.opencsv.CSVWriter.DEFAULT_SEPARATOR;
import static au.com.bytecode.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

@Component
public class CSVWriter {

    public void writeCSVData(Writer writer, CSVExportProcessor csvExportProcessor, Object parameters) throws IOException {
        ExportData exportData = csvExportProcessor.getCSVData(parameters);
        writeDataToCSV(writer, exportData);
    }

    public void writeCSVFromData(Writer writer, List data) throws IOException {
        ExportData exportData = new CSVExportProcessor().formatCSVData(data);
        writeDataToCSV(writer, exportData);
    }

    private void writeDataToCSV(Writer writer, ExportData exportData) throws IOException {
        au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(writer, DEFAULT_SEPARATOR, NO_QUOTE_CHARACTER);
        csvWriter.writeNext(exportData.getColumnHeaders().toArray(new String[]{}));
        csvWriter.writeAll(getData(exportData.getAllRowData()));
        csvWriter.close();
    }

    private List<String[]> getData(List<List<String>> allRowData) {
        List<String[]> rows = new ArrayList<String[]>();
        for (List<String> row : allRowData) {
            rows.add(row.toArray(new String[]{}));
        }
        return rows;
    }
}
