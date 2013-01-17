package org.motechproject.jasper.reports;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.motechproject.jasper.reports.util.JasperRESTClient;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ReportsMigration implements JavaMigration {

    private static final String REPORTS_PATH = "/Reports/";
    private static final String IMPORT_COMMAND = "cd $JASPER_HOME/buildomatic/ && $JASPER_HOME/buildomatic/js-import.sh --input-zip %s --update";

    private final ReportsProperties reportsProperties;

    public ReportsMigration() {
        reportsProperties = new ReportsProperties();
    }

    public abstract String[] getReportNamesToAdd();

    public abstract String[] getReportNamesToDelete();

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        processReportDeletion(getReportNamesToDelete());
        processReportsImport(getReportNamesToAdd());
    }

    private void processReportsImport(String[] reportNamesToAdd) throws Exception {
        for (String reportNameToAdd : reportNamesToAdd) {
            processImportOfReportWithName(reportNameToAdd);
        }
    }

    private void processReportDeletion(String[] reportNamesToDelete) {
        JasperRESTClient jasperRESTClient = new JasperRESTClient();
        for (String reportNameToDelete : reportNamesToDelete) {
            jasperRESTClient.deleteResource(reportsProperties.getReportsRootLocation() + REPORTS_PATH + reportNameToDelete);
        }
    }

    private void processImportOfReportWithName(String reportName) throws Exception {
        File file = new File(reportsProperties.getReportsSourceLocation() + File.separator + reportName + ".zip");
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