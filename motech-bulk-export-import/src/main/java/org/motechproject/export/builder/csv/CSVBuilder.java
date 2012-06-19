package org.motechproject.export.builder.csv;

import org.motechproject.export.model.ExcelExportProcessor;
import org.motechproject.export.model.ExportData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

//To be used if data is to be written to a file
public class CSVBuilder {

    private File file;
    private ExcelExportProcessor excelExportProcessor;
    private Map<String, String> criteria;
    private String reportName;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public CSVBuilder(String fileName, String reportName, ExcelExportProcessor excelExportProcessor, Map<String, String> criteria) {
        this.reportName = reportName;
        this.excelExportProcessor = excelExportProcessor;
        this.criteria = criteria;
        file = (fileName == null) ? new File(excelExportProcessor.name() + "-report.csv") : new File(fileName);
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
        ExportData export = excelExportProcessor.getEntirExcelData(reportName, criteria);
        bufferedWriter.write(getCsvRow(export.getColumnHeaders()));

        for (List<String> row : export.getAllRowData())
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
