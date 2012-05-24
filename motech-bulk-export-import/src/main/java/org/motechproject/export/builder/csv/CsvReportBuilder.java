package org.motechproject.export.builder.csv;

import org.motechproject.export.model.ReportData;
import org.motechproject.export.model.ReportDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvReportBuilder {

    private File file;
    private ReportDataSource reportDataSource;
    private String reportName;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public CsvReportBuilder(String fileName, String reportName, ReportDataSource reportDataSource) {
        this.reportName = reportName;
        this.reportDataSource = reportDataSource;
        file = new File(fileName);
    }

    public File build() {
        try {
            writeCsvReportToFile();
        } catch (IOException e) {
            logger.error("Error while creating a csv report: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return file;
    }

    private void writeCsvReportToFile() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        ReportData report = reportDataSource.createEntireReport(reportName);
        bufferedWriter.write(getCsvRow(report.getColumnHeaders()));

        for (List<String> row : report.getAllRowData())
            bufferedWriter.write(getCsvRow(row));

        bufferedWriter.close();
    }

    private String getCsvRow(List<String> dataList) {
        StringBuilder result = new StringBuilder();
        for (String datum : dataList) {
            result.append(datum);
            result.append(",");
        }
        String csvRow = result.toString();
        return replaceTrailingCommaByNewline(csvRow);
    }

    private String replaceTrailingCommaByNewline(String csvRow) {
        int lastButOneCharacterIndex = csvRow.length() - 2;
        return csvRow.substring(0, lastButOneCharacterIndex).concat("\n");
    }
}
