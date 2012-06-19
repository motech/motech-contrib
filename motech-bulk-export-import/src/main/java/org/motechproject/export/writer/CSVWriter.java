package org.motechproject.export.writer;

import org.motechproject.export.model.CSVReportDataSource;
import org.motechproject.export.model.ReportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static au.com.bytecode.opencsv.CSVWriter.DEFAULT_SEPARATOR;
import static au.com.bytecode.opencsv.CSVWriter.NO_QUOTE_CHARACTER;

@Component
public class CSVWriter {

    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String TEXT_CSV = "text/csv";

    public void writeCSVToResponse(HttpServletResponse response, CSVReportDataSource csvReportDataSource, String fileName) {
        try {
            ReportData reportData = csvReportDataSource.createEntireReport();
            initializeCSVResponse(response, fileName);
            PrintWriter responseWriter = response.getWriter();
            au.com.bytecode.opencsv.CSVWriter csvWriter = new au.com.bytecode.opencsv.CSVWriter(responseWriter, DEFAULT_SEPARATOR, NO_QUOTE_CHARACTER);
            csvWriter.writeNext(reportData.getColumnHeaders().toArray(new String[]{}));
            csvWriter.writeAll(getData(reportData.getAllRowData()));
            responseWriter.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response: " + e.getMessage());
        }
    }

    private List<String[]> getData(List<List<String>> allRowData) {
        List<String[]> rows = new ArrayList<String[]>();
        for (List<String> row : allRowData) {
            rows.add(row.toArray(new String[]{}));
        }
        return rows;
    }

    private void initializeCSVResponse(HttpServletResponse response, String fileName) {
        response.setHeader(CONTENT_DISPOSITION, "inline; filename=" + fileName);
        response.setContentType(TEXT_CSV);
    }
}
