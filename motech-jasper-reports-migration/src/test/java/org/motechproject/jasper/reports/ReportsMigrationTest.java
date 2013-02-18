package org.motechproject.jasper.reports;

import db.migration.domain.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

@Ignore
public class ReportsMigrationTest {

    @Test
    public void shouldMigrateReportWithGivenNames() throws Exception {
        TestReport testReport = new TestReport();
        testReport.migrate(null);
    }

    class TestReport extends ReportsMigration {
        @Override
        public Report[] getReportsToAdd() {
            return new Report[]{
                    new Report("SubscriptionsPerPackReport", new EntityResource(new ArrayList<PermissionItem>(){{
                        add(new PermissionItem(AccessRights.READ_ONLY.getPermissionMask(),new PermissionRecipient("Role"), "uri"));
                    }}))
            };
        }

        @Override
        public String[] getReportNamesToDelete() {
            return new String[0];
        }
    }
}
