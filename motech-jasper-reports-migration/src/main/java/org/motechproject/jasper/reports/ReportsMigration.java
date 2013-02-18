package org.motechproject.jasper.reports;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import db.migration.domain.EntityResource;
import db.migration.domain.Report;
import org.apache.commons.lang.StringUtils;
import org.motechproject.jasper.reports.util.JasperRESTClient;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ReportsMigration implements JavaMigration {

    private static final String REPORTS_PATH = "/Reports/";
    private static final String IMPORT_COMMAND = "cd $JASPER_HOME/buildomatic/ && $JASPER_HOME/buildomatic/js-import.sh --input-dir %s --update";

    private final ReportsProperties reportsProperties;
    private JasperRESTClient jasperRESTClient;

    public ReportsMigration() {
        jasperRESTClient = new JasperRESTClient();
        reportsProperties = new ReportsProperties();
    }

    public abstract Report[] getReportsToAdd();

    public abstract String[] getReportNamesToDelete();

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        processReportsDeletion(getReportNamesToDelete());
        for (Report report : getReportsToAdd()) {
            processReportImport(report);
            processReportsPermission(report.getResource());
        }
    }

    private void processReportsPermission(EntityResource entityResource) throws Exception {
        if (entityResource == null) return;
        jasperRESTClient.put(reportsProperties.getJasperPermissionsURL(), entityResource);
    }

    private void processReportsDeletion(String[] reportNamesToDelete) {
        for (String reportNameToDelete : reportNamesToDelete) {
            jasperRESTClient.deleteResource(reportsProperties.getReportsRootLocation() + REPORTS_PATH + reportNameToDelete);
        }
    }

    private void processReportImport(Report report) throws Exception {
        String reportName = report.getName();

        if (StringUtils.isBlank(reportName)) return;

        File file = new File(reportsProperties.getReportsSourceLocation() + File.separator + reportName);
        if (!file.exists())
            throw new RuntimeException("Report " + reportName + " does not exist in the resources directory at location " + file.getPath());
        System.out.println(String.format("Importing of report %s started.", reportName));
        importReport(file);
        System.out.println(String.format("Importing of report %s completed.", reportName));
    }

    private void importReport(File file) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-l", "-c", String.format(IMPORT_COMMAND, file.getAbsolutePath())});
        process.waitFor();
        logInputStream(process);
        if (process.exitValue() != 0) {
            logErrorStream(process);
            throw new RuntimeException(String.format("Error occurred during import of report %s. See logs for more info.", file.getName()));
        }
    }

    private void logInputStream(Process process) throws IOException {
        System.out.println("Report task output: ");
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = buf.readLine()) != null)
            System.out.println(line);
    }

    private void logErrorStream(Process process) throws IOException {
        System.err.println("Report task error output: ");
        BufferedReader buf;
        String line;
        buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = buf.readLine()) != null)
            System.err.println(line);
    }
}