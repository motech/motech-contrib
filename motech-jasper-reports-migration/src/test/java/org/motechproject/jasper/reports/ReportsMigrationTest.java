package org.motechproject.jasper.reports;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ReportsMigrationTest {

    @Test
    public void shouldMigrateReportWithGivenNames() throws Exception {
        TestReport testReport = new TestReport();
        testReport.migrate(null);
    }

    class TestReport extends ReportsMigration {
        @Override
        public String[] getReportNamesToAdd() {
            return new String[]{"SubscriptionsPerPackReport"};
        }

        @Override
        public String[] getReportNamesToDelete() {
            return new String[0];
        }
    }
}
