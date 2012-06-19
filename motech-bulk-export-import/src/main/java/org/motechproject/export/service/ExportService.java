package org.motechproject.export.service;

import org.motechproject.export.model.AllCSVExportProcessors;
import org.motechproject.export.model.AllExcelExportProcessors;
import org.motechproject.export.model.CSVExportProcessor;
import org.motechproject.export.model.ExcelExportProcessor;
import org.motechproject.export.writer.CSVWriter;
import org.motechproject.export.writer.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.Writer;


@Service
public class ExportService {

    private AllExcelExportProcessors allExcelExportProcessors;
    private AllCSVExportProcessors allCSVExportProcessors;
    private ExcelWriter excelWriter;
    private CSVWriter csvWriter;

    @Autowired
    public ExportService(AllExcelExportProcessors allExcelExportProcessors, AllCSVExportProcessors allCSVExportProcessors,
                         ExcelWriter excelWriter, CSVWriter csvWriter) {
        this.allExcelExportProcessors = allExcelExportProcessors;
        this.allCSVExportProcessors = allCSVExportProcessors;
        this.excelWriter = excelWriter;
        this.csvWriter = csvWriter;
    }

    public void exportAsExcel(String groupName, String reportName, OutputStream outputStream) {
        ExcelExportProcessor excelExportProcessor = allExcelExportProcessors.get(groupName);
        excelWriter.writeExcelToResponse(outputStream, excelExportProcessor, reportName, reportName + ".xls");
    }

    public void exportAsCSV(String dataSourceName, Writer writer) {
        CSVExportProcessor csvExportProcessor = allCSVExportProcessors.get(dataSourceName);
        csvWriter.writeCSVData(writer, csvExportProcessor);
    }
}
