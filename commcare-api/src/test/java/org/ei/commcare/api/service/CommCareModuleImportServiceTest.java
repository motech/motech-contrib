package org.ei.commcare.api.service;

import org.ei.commcare.api.contract.CommCareFormDefinition;
import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.util.CommCareImportProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.scheduler.gateway.OutboundEventGateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.ei.commcare.api.util.CommCareImportProperties.COMMCARE_IMPORT_DEFINITION_FILE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommCareModuleImportServiceTest {
    @Mock
    private CommCareFormImportService commcareFormImportService;
    @Mock
    private OutboundEventGateway outboundEventGateway;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldFetchFormsWhenThereIsOneModule() throws Exception {
        CommCareImportProperties commCareImportProperties = properties("/test-data/commcare-export-with-one-url.json");
        List<CommCareFormDefinition> formDefinitions = commCareImportProperties.moduleDefinitions().modules().get(0).definitions();
        CommCareFormInstance formInstance = new CommCareFormInstance(formDefinitions.get(0), new CommCareFormContent(asList("key"), asList("value")));
        when(commcareFormImportService.fetchForms(formDefinitions, "www.server.org", "someUser@gmail.com", "somePassword")).thenReturn(Arrays.asList(formInstance));

        CommCareModuleImportService moduleImportService = new CommCareModuleImportService(commcareFormImportService, commCareImportProperties);
        List<List<CommCareFormInstance>> formsForEachModule = moduleImportService.fetchFormsForAllModules();

        verify(commcareFormImportService).fetchForms(formDefinitions, "www.server.org", "someUser@gmail.com", "somePassword");
        assertEquals(1, formsForEachModule.size());
        assertEquals(asList(formInstance), formsForEachModule.get(0));
    }

    @Test
    public void shouldFetchAllFormsOfAllModules() throws Exception {
        CommCareImportProperties commCareImportProperties = properties("/test-data/commcare-export-with-two-modules.json");
        List<CommCareFormDefinition> firstModuleFormDefinitions = commCareImportProperties.moduleDefinitions().modules().get(0).definitions();
        List<CommCareFormDefinition> secondModuleFormDefinitions = commCareImportProperties.moduleDefinitions().modules().get(1).definitions();

        CommCareFormInstance firstModuleFormInstance = new CommCareFormInstance(firstModuleFormDefinitions.get(0), new CommCareFormContent(asList("key"), asList("value")));
        CommCareFormInstance secondModuleFormInstance = new CommCareFormInstance(secondModuleFormDefinitions.get(0), new CommCareFormContent(asList("key"), asList("value")));
        when(commcareFormImportService.fetchForms(firstModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword")).thenReturn(Arrays.asList(firstModuleFormInstance));
        when(commcareFormImportService.fetchForms(secondModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword")).thenReturn(Arrays.asList(secondModuleFormInstance));

        CommCareModuleImportService moduleImportService = new CommCareModuleImportService(commcareFormImportService, commCareImportProperties);
        List<List<CommCareFormInstance>> allForms = moduleImportService.fetchFormsForAllModules();

        verify(commcareFormImportService).fetchForms(firstModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword");
        verify(commcareFormImportService).fetchForms(secondModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword");
        assertEquals(2, allForms.size());
        assertEquals(asList(firstModuleFormInstance), allForms.get(0));
        assertEquals(asList(secondModuleFormInstance), allForms.get(1));
    }

    @Test
    public void shouldNotGiveFormsForModulesForWhichNoFormsWereFetched() throws Exception {
        CommCareImportProperties commCareImportProperties = properties("/test-data/commcare-export-with-two-modules.json");
        List<CommCareFormDefinition> firstModuleFormDefinitions = commCareImportProperties.moduleDefinitions().modules().get(0).definitions();
        List<CommCareFormDefinition> secondModuleFormDefinitions = commCareImportProperties.moduleDefinitions().modules().get(1).definitions();

        CommCareFormInstance firstModuleFormInstance = new CommCareFormInstance(firstModuleFormDefinitions.get(0), new CommCareFormContent(asList("key"), asList("value")));
        when(commcareFormImportService.fetchForms(firstModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword")).thenReturn(Arrays.asList(firstModuleFormInstance));
        when(commcareFormImportService.fetchForms(secondModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword")).thenReturn(new ArrayList<CommCareFormInstance>());

        CommCareModuleImportService moduleImportService = new CommCareModuleImportService(commcareFormImportService, commCareImportProperties);
        List<List<CommCareFormInstance>> allForms = moduleImportService.fetchFormsForAllModules();

        verify(commcareFormImportService).fetchForms(firstModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword");
        verify(commcareFormImportService).fetchForms(secondModuleFormDefinitions, "www.server.org", "someUser@gmail.com", "somePassword");
        assertEquals(1, allForms.size());
        assertEquals(asList(firstModuleFormInstance), allForms.get(0));
    }

    private CommCareImportProperties properties(String fileName) {
        Properties properties = new Properties();
        properties.setProperty(COMMCARE_IMPORT_DEFINITION_FILE, fileName);
        return new CommCareImportProperties(properties);
    }
}
