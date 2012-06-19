package org.motechproject.export.controller;

import org.motechproject.export.model.AllCSVReportDataSources;
import org.motechproject.export.model.AllExcelReportDataSources;
import org.motechproject.export.model.CSVReportDataSource;
import org.motechproject.export.model.ExcelReportDataSource;
import org.motechproject.export.writer.CSVWriter;
import org.motechproject.export.writer.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@RequestMapping(value = "/reports")
@Controller
public class ReportController {

    private AllExcelReportDataSources allExcelReportDataSources;
    private AllCSVReportDataSources allCSVReportDataSources;
    private ExcelWriter excelWriter;
    private CSVWriter csvWriter;

    @Autowired
    public ReportController(AllExcelReportDataSources allExcelReportDataSources, AllCSVReportDataSources allCSVReportDataSources, ExcelWriter excelWriter, CSVWriter csvWriter) {
        this.allExcelReportDataSources = allExcelReportDataSources;
        this.allCSVReportDataSources = allCSVReportDataSources;
        this.excelWriter = excelWriter;
        this.csvWriter = csvWriter;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{groupName}/{reportName}.xls")
    public void createExcelReport(@PathVariable("groupName") String groupName, @PathVariable("reportName") String reportName,
                                  HttpServletResponse response) {
        ExcelReportDataSource excelReportDataSource = allExcelReportDataSources.get(groupName);
        excelWriter.writeExcelToResponse(response, excelReportDataSource, reportName, reportName + ".xls");
    }

    @RequestMapping(method = RequestMethod.GET, value = "{groupName}/{reportName}.csv")
    public void createCSVReport(@PathVariable("groupName") String groupName, @PathVariable("reportName") String reportName,
                                HttpServletResponse response) {
        CSVReportDataSource csvReportDataSource = allCSVReportDataSources.get(groupName);
        csvWriter.writeCSVToResponse(response, csvReportDataSource, reportName + ".csv");
    }
}
