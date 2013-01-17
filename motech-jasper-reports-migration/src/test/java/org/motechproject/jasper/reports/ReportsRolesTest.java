package org.motechproject.jasper.reports;

import db.migration.domain.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.jasper.reports.util.JasperRESTClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportsRolesTest {
    @Mock
    private JasperRESTClient jasperRESTClient;
    @Mock
    private ReportsProperties reportsProperties;
    private TestRoles roles;

    @Before
    public void setup() {
        roles = new TestRoles(jasperRESTClient, reportsProperties);
    }

    @Test
    public void shouldCreateRole() throws Exception {
        String url = "/jasperserver/rest/roles/";
        when(reportsProperties.getJasperRoleCreationURL()).thenReturn(url);

        roles.migrate(null);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(jasperRESTClient, times(2)).put(eq(url), captor.capture());
        List<Role> actualRequestBody = captor.getAllValues();
        assertEquals(2, actualRequestBody.size());
        assertEquals(new Role("ROLE_TEST1"), actualRequestBody.get(0));
        assertEquals(new Role("ROLE_TEST2"), actualRequestBody.get(1));
    }

    private class TestRoles extends ReportsRoles {
        TestRoles(JasperRESTClient jasperRESTClient, ReportsProperties reportsProperties) {
            super(jasperRESTClient, reportsProperties);
        }

        @Override
        protected List<Role> getRolesToCreate() {
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(new Role("ROLE_TEST1"));
            roles.add(new Role("ROLE_TEST2"));
            return roles;
        }
    }
}
