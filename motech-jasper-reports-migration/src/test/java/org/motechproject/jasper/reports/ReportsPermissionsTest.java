package org.motechproject.jasper.reports;

import db.migration.domain.EntityResource;
import db.migration.domain.Item;
import db.migration.domain.PermissionRecipient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.jasper.reports.util.JasperRESTClient;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportsPermissionsTest {
    @Mock
    private JasperRESTClient jasperRESTClient;
    @Mock
    private ReportsProperties reportsProperties;
    private TestPermissions permissions;

    @Before
    public void setup() {
        permissions = new TestPermissions(jasperRESTClient, reportsProperties);
    }

    @Test
    public void shouldSetAppropriatePermissions() throws Exception {
        String url = "/jasperserver/rest/permission/";
        when(reportsProperties.getJasperPermissionsURL()).thenReturn(url);

        permissions.migrate(null);

        ArgumentCaptor<EntityResource> captor = ArgumentCaptor.forClass(EntityResource.class);
        verify(jasperRESTClient).put(eq(url), captor.capture());
        EntityResource actualRequestBody = captor.getValue();
        assertEquals(2, actualRequestBody.getItem().size());
        assertEquals(new Item(AccessRights.READ_ONLY.getPermissionMask(), new PermissionRecipient("ROLE 1"), "repo:/Report1"), actualRequestBody.getItem().get(0));
        assertEquals(new Item(AccessRights.NO_ACCESS.getPermissionMask(), new PermissionRecipient("ROLE 2"), "repo:/Report2"), actualRequestBody.getItem().get(1));
    }

    private class TestPermissions extends ReportsPermissions {
        private TestPermissions(JasperRESTClient jasperRESTClient, ReportsProperties reportsProperties) {
            super(jasperRESTClient, reportsProperties);
        }

        @Override
        protected EntityResource getResourcePermissions() {
            ArrayList<Item> items = new ArrayList<>();
            items.add(new Item(AccessRights.READ_ONLY.getPermissionMask(), new PermissionRecipient("ROLE 1"), "repo:/Report1"));
            items.add(new Item(AccessRights.NO_ACCESS.getPermissionMask(), new PermissionRecipient("ROLE 2"), "repo:/Report2"));
            return new EntityResource(items);
        }
    }
}
