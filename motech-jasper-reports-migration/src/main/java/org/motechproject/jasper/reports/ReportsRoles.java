package org.motechproject.jasper.reports;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import db.migration.domain.Role;
import org.motechproject.jasper.reports.util.JasperRESTClient;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public abstract class ReportsRoles implements JavaMigration {

    private JasperRESTClient jasperRESTClient;
    private ReportsProperties reportsProperties;

    public ReportsRoles() {
        jasperRESTClient = new JasperRESTClient();
        reportsProperties = new ReportsProperties();
    }

    public ReportsRoles(JasperRESTClient jasperRESTClient, ReportsProperties reportsProperties) {
        this.jasperRESTClient = jasperRESTClient;
        this.reportsProperties = reportsProperties;
    }

    protected abstract List<Role> getRolesToCreate();

    @Override
    public final void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        for (Role role : getRolesToCreate()) {
            try {
                jasperRESTClient.put(reportsProperties.getJasperRoleCreationURL(), role);
            } catch (HttpClientErrorException e) {
                if (!(e.getStatusCode() == HttpStatus.BAD_REQUEST && e.getResponseBodyAsString().matches("^(can not create new role:).+(it already exists)$")))
                    throw e;
            }
        }
    }
}