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
import java.util.Map;

public class CsvReportBuilder {

    private File file;
    private ReportDataSource reportDataSource;
    private Map<String, String> criteria;
    private String reportName;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public CsvReportBuilder(String fileName, String reportName, ReportDataSource reportDataSource, Map<String, String> criteria) {
        this.reportName = reportName;
        this.reportDataSource = reportDataSource;
        this.criteria = criteria;
        file = (fileName == null) ? new File(reportDataSource.name() + "-report.csv") : new File(fileName);
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
        ReportData report = reportDataSource.createEntireReport(reportName, criteria);
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
        int lastCharacterIndex = csvRow.length() - 1;
        return csvRow.substring(0, lastCharacterIndex).concat("\n");
    }
}
