package org.ei.commcare.api.service;

import org.apache.http.Header;
import org.ei.commcare.api.contract.CommCareModuleDefinitions;
import org.ei.commcare.api.domain.CaseInformation;
import org.ei.commcare.api.util.CommCareHttpClient;
import org.ei.commcare.api.util.CommCareHttpResponse;
import org.ei.commcare.api.util.CommCareImportProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.StringReader;

import static junit.framework.Assert.*;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommCareCaseImportServiceTest {
    @Mock
    private CommCareHttpClient commCareHttpClient;
    @Mock
    private CommCareImportProperties commCareImportProperties;
    @Mock
    private CommCareModuleDefinitions commCareModuleDefinitions;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setupDefinitions();
    }

    @Test
    public void shouldFetchDependentsCaseWhenParentCaseIdIsProvided() throws Exception {
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=MOTHER-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], jsonFor("mother-cloud-care.json")));
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=CHILD-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], jsonFor("child-cloud-care.json")));

        CommCareCaseImportService service = new CommCareCaseImportService(commCareHttpClient, commCareImportProperties);
        CaseInformation childCaseInformation = service.fetchDependentCase("MOTHER-CASE-X");

        assertTrue(childCaseInformation.isValid());
        assertEquals(4, childCaseInformation.properties().size());
        assertEquals(" ", childCaseInformation.properties().get("child_DOB"));
        assertEquals("bcg opv0", childCaseInformation.properties().get("immunization_multiselect"));
        assertEquals("2012-06-13", childCaseInformation.properties().get("immunization_servicedate"));
        assertEquals("1345677", childCaseInformation.properties().get("mother_thayino"));
    }

    @Test
    public void shouldReturnCaseInformationWithFailedStatusWhenMotherCaseInformationCannotBeFetched() throws Exception {
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=MOTHER-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(404, new Header[0], new byte[0]));

        CommCareCaseImportService service = new CommCareCaseImportService(commCareHttpClient, commCareImportProperties);
        CaseInformation childCaseInformation = service.fetchDependentCase("MOTHER-CASE-X");

        assertFalse(childCaseInformation.isValid());
    }

    @Test
    public void shouldReturnCaseInformationWithFailedStatusWhenChildCaseInformationCannotBeFetched() throws Exception {
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=MOTHER-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], jsonFor("mother-cloud-care.json")));
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=CHILD-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(404, new Header[0], new byte[0]));

        CommCareCaseImportService service = new CommCareCaseImportService(commCareHttpClient, commCareImportProperties);
        CaseInformation childCaseInformation = service.fetchDependentCase("MOTHER-CASE-X");

        assertFalse(childCaseInformation.isValid());
    }

    @Test
    public void shouldReturnCaseInformationWithFailedStatusWhenMotherCaseInformationIsEmptyOrNotFound() throws Exception {
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=MOTHER-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], toByteArray(new StringReader("[]"))));

        CommCareCaseImportService service = new CommCareCaseImportService(commCareHttpClient, commCareImportProperties);
        CaseInformation childCaseInformation = service.fetchDependentCase("MOTHER-CASE-X");

        assertFalse(childCaseInformation.isValid());
    }

    @Test
    public void shouldReturnCaseInformationWithFailedStatusWhenChildCaseInformationIsEmptyOrNotFound() throws Exception {
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=MOTHER-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], jsonFor("mother-cloud-care.json")));
        when(commCareHttpClient.get("http://commcare.org/cloudcare/?case_id=CHILD-CASE-X", "www.server.org", "username", "password")).thenReturn(new CommCareHttpResponse(200, new Header[0], toByteArray(new StringReader("[]"))));

        CommCareCaseImportService service = new CommCareCaseImportService(commCareHttpClient, commCareImportProperties);
        CaseInformation childCaseInformation = service.fetchDependentCase("MOTHER-CASE-X");

        assertFalse(childCaseInformation.isValid());
    }

    private void setupDefinitions() {
        when(commCareImportProperties.moduleDefinitions()).thenReturn(commCareModuleDefinitions);
        when(commCareModuleDefinitions.userName()).thenReturn("username");
        when(commCareModuleDefinitions.password()).thenReturn("password");
        when(commCareModuleDefinitions.commcareBaseUrl()).thenReturn("www.server.org");
        when(commCareModuleDefinitions.caseFetchURLBase()).thenReturn("http://commcare.org/cloudcare/");
    }

    private byte[] jsonFor(String jsonFileName) throws IOException {
        return toByteArray(getClass().getResourceAsStream("/test-data/" + jsonFileName));
    }
}
