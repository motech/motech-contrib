package org.motechproject.export.writer;

import org.motechproject.export.model.CSVExportProcessor;
import org.motechproject.export.model.ExportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static au.com.bytecode.opencsv.CSVWriter.DEFAULT_SEPARATOR;
import static au.com.bytecode.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

@Component
public class CSVWriter {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void writeCSVData(Writer writer, CSVExportProcessor csvExportProcessor) {
        ExportData exportData = csvExportProcessor.createEntireReport();
        au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(writer, DEFAULT_SEPARATOR, NO_QUOTE_CHARACTER);
        csvWriter.writeNext(exportData.getColumnHeaders().toArray(new String[]{}));
        csvWriter.writeAll(getData(exportData.getAllRowData()));
    }

    private List<String[]> getData(List<List<String>> allRowData) {
        List<String[]> rows = new ArrayList<String[]>();
        for (List<String> row : allRowData) {
            rows.add(row.toArray(new String[]{}));
        }
        return rows;
    }
}
