package org.motechproject.jasper.reports;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import db.migration.domain.EntityResource;
import org.motechproject.jasper.reports.util.JasperRESTClient;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class ReportsPermissions implements JavaMigration {
    private JasperRESTClient jasperRESTClient;
    private ReportsProperties reportsProperties;

    public ReportsPermissions() {
        jasperRESTClient = new JasperRESTClient();
        reportsProperties = new ReportsProperties();
    }

    public ReportsPermissions(JasperRESTClient jasperRESTClient, ReportsProperties reportsProperties) {
        this.jasperRESTClient = jasperRESTClient;
        this.reportsProperties = reportsProperties;
    }

    protected abstract EntityResource getResourcePermissions();

    @Override
    public final void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jasperRESTClient.put(reportsProperties.getJasperPermissionsURL(), getResourcePermissions());
    }
}
