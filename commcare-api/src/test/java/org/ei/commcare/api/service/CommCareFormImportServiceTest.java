package org.ei.commcare.api.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.ei.commcare.api.contract.CommCareFormDefinition;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.domain.ExportToken;
import org.ei.commcare.api.repository.AllExportTokens;
import org.ei.commcare.api.util.CommCareHttpClient;
import org.ei.commcare.api.util.CommCareHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.commcare.api.util.CommCareFormDefinitionBuilder.createForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

public class CommCareFormImportServiceTest {
    public static final String NAMESPACE2 = "http://harshit.com";
    @Mock
    private CommCareHttpClient httpClient;
    @Mock
    private CredentialsProvider provider;
    @Mock
    private HttpResponse response;
    @Mock
    private AllExportTokens allExportTokens;
    private CommCareFormImportService formImportService;
    public static final String NAMESPACE = "http://openrosa.org/formdesigner/4FBE07FF-2434-40B3-B151-D2EBE2F4FB4F";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        formImportService = new CommCareFormImportService(allExportTokens, httpClient);
    }

    @Test
    public void shouldFetchOneFormWithTwoInstancesFromCommCare() throws Exception {
        String urlOfExport = "http://baseURL__FORM__?export_tag=%22" + NAMESPACE2 + "%22&format=json&previous_export=";
        CommCareFormDefinition formDefinition = setupForm("__FORM__", "", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));

        List<CommCareFormInstance> formInstances = formImportService.fetchForms(asList(formDefinition), "user", "password");

        verify(httpClient).get(urlOfExport, "user", "password");
        assertEquals(2, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"form-1-instance-1-value-1", "form-1-instance-1-value-2"}, "__FORM__", "extraFieldValue-1-1");
        assertForm(formInstances.get(1), new String[]{"form-1-instance-2-value-1", "form-1-instance-2-value-2"}, "__FORM__", "extraFieldValue-1-2");
    }

    @Test
    public void shouldConvertNullValuesInCommCareFormExportAsEmptyStrings() throws Exception {
        String urlOfExport = "http://baseURL__FORM__?export_tag=%22" + NAMESPACE2 + "%22&format=json&previous_export=";
        CommCareFormDefinition formDefinition = setupForm("__FORM__", "", formResponse(200, "/test-data/form.3.dump.json", "NEW-TOKEN"));

        List<CommCareFormInstance> formInstances = formImportService.fetchForms(asList(formDefinition), "user", "password");

        verify(httpClient).get(urlOfExport, "user", "password");
        assertEquals(2, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"form-1-instance-1-value-1", ""}, "__FORM__", "extraFieldValue-1-1");
        assertForm(formInstances.get(1), new String[]{"form-1-instance-2-value-1", "form-1-instance-2-value-2"}, "__FORM__", "");
    }

    @Test
    public void shouldFetchMultipleFormsWithMultipleInstancesFromCommCare() throws Exception {
        String urlOfFirstExport = "http://baseURL__FORM1__?export_tag=%22" + NAMESPACE + "%22&format=json&previous_export=OLD-TOKEN";
        String urlOfSecondExport = "http://baseURL__FORM2__?export_tag=%22" + NAMESPACE2 + "%22&format=json&previous_export=OLD-TOKEN";

        CommCareFormDefinition firstFormDefinition = setupForm("__FORM1__", NAMESPACE, "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));
        CommCareFormDefinition secondFormDefinition = setupForm("__FORM2__", NAMESPACE2, "OLD-TOKEN", formResponse(200, "/test-data/form.2.dump.json", "NEW-TOKEN"));

        List<CommCareFormInstance> formInstances = formImportService.fetchForms(asList(firstFormDefinition, secondFormDefinition), "user", "password");

        verify(httpClient).get(urlOfFirstExport, "user", "password");
        verify(httpClient).get(urlOfSecondExport, "user", "password");
        verify(allExportTokens).updateToken(NAMESPACE, "NEW-TOKEN");
        verify(allExportTokens).updateToken(NAMESPACE2, "NEW-TOKEN");

        assertEquals(4, formInstances.size());
        assertForm(formInstances.get(0), new String[]{"form-1-instance-1-value-1", "form-1-instance-1-value-2"}, "__FORM1__", "extraFieldValue-1-1");
        assertForm(formInstances.get(1), new String[]{"form-1-instance-2-value-1", "form-1-instance-2-value-2"}, "__FORM1__", "extraFieldValue-1-2");
        assertForm(formInstances.get(2), new String[]{"form-2-instance-1-value-1", "form-2-instance-1-value-2"}, "__FORM2__", "extraFieldValue-2-1");
        assertForm(formInstances.get(3), new String[]{"form-2-instance-2-value-1", "form-2-instance-2-value-2"}, "__FORM2__", "extraFieldValue-2-2");
    }

    @Test
    public void shouldUseURLWithoutPreviousTokenWhenThereIsNoToken() throws Exception {
        String urlOfExport = "http://baseURL?export_tag=%22" + NAMESPACE2 + "%22&format=json&previous_export=";
        CommCareFormDefinition form = setupForm("", "", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));

        formImportService.fetchForms(asList(form), "user", "password");

        verify(httpClient).get(urlOfExport, "user", "password");
    }

    @Test
    public void shouldUseURLWithPreviousTokenWhenThereIsAToken() throws Exception {
        String urlOfExport = "http://baseURL?export_tag=%22" + NAMESPACE2 + "%22&format=json&previous_export=OLD-TOKEN";
        CommCareFormDefinition form = setupForm("", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));

        formImportService.fetchForms(asList(form), "user", "password");

        verify(httpClient).get(urlOfExport, "user", "password");
    }

    @Test
    public void shouldSaveTheExportTokenAfterFetchingData() throws Exception {
        CommCareFormDefinition form = setupForm("", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));

        formImportService.fetchForms(asList(form), "user", "password");

        verify(allExportTokens).updateToken(NAMESPACE2, "NEW-TOKEN");
    }

    @Test
    public void shouldNotProcessFormOrUpdateTokenWhenResponseSaysThatThereIsNoNewData() throws Exception {
        CommCareFormDefinition formWithEmptyData = setupForm("", "OLD-TOKEN", formResponse(302, "/test-data/form.1.dump.json", null));

        List<CommCareFormInstance> formInstances = formImportService.fetchForms(asList(formWithEmptyData), "user", "password");

        assertThat(formInstances.size(), is(0));
        verify(allExportTokens).findByNameSpace(NAMESPACE2);
        verifyNoMoreInteractions(allExportTokens);
    }

    @Test
    public void shouldFetchNoFormInstancesWhenItFailsToFetchAtLeastOneFormDefinition() throws Exception {
        CommCareFormDefinition form1 = setupForm("__FORM1__", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));
        CommCareFormDefinition form2 = setupForm("__FORM2__", "OLD-TOKEN", formResponse(302, "/test-data/form.with.empty.data.json", null));

        List<CommCareFormInstance> instances = formImportService.fetchForms(asList(form1, form2), "user", "password");

        assertThat(instances.size(), is(2));
        verify(allExportTokens).updateToken(NAMESPACE2, "NEW-TOKEN");
    }

    @Test
    public void shouldUpdateOnlyTokensForThoseCallsWhichHadNewData() throws Exception {
        CommCareFormDefinition form1 = setupForm("FORM1", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));
        CommCareFormDefinition form2 = setupForm("FORM2", "OLD-TOKEN", formResponse(302, "/test-data/form.with.empty.data.json", null));
        CommCareFormDefinition form3 = setupForm("FORM3", "OLD-TOKEN", formResponse(302, "/test-data/form.with.empty.data.json", null));

        formImportService.fetchForms(asList(form1, form2, form3), "user", "password");

        verify(allExportTokens, times(1)).updateToken(NAMESPACE2, "NEW-TOKEN");
    }

    @Test
    public void shouldNotUpdateAnyTokensWhenItFailsToFetchAtLeastOneFormDefinition() throws Exception {
        CommCareFormDefinition form1 = setupForm("FORM1", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));
        CommCareFormDefinition form2 = setupForm("FORM2", "OLD-TOKEN", formResponse(404, "/test-data/form.1.dump.json", null));
        CommCareFormDefinition form3 = setupForm("FORM3", "OLD-TOKEN", formResponse(200, "/test-data/form.1.dump.json", "NEW-TOKEN"));

        formImportService.fetchForms(asList(form1, form2, form3), "user", "password");

        verify(allExportTokens, times(0)).updateToken(Matchers.<String>any(), Matchers.<String>any());
    }

    private CommCareFormDefinition setupForm(String formPrefix, String oldToken, CommCareHttpResponse httpResponse) throws IOException {
        return setupForm(formPrefix, NAMESPACE2, oldToken, httpResponse);
    }

    private CommCareFormDefinition setupForm(String formPrefix, String nameSpace, String oldToken, CommCareHttpResponse httpResponse) {
        CommCareFormDefinition formDefinition = createForm(formPrefix, nameSpace);

        when(allExportTokens.findByNameSpace(nameSpace)).thenReturn(new ExportToken(nameSpace, oldToken));
        when(httpClient.get(formDefinition.url(oldToken), "user", "password")).thenReturn(httpResponse);
        return formDefinition;
    }

    private CommCareHttpResponse formResponse(int statusCode, String jsonDump, String newTokenValue) throws IOException {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("X-Abc-Header", "Def"));

        if (newTokenValue != null) {
            headers.add(new BasicHeader("X-CommCareHQ-Export-Token", newTokenValue));
        }

        return new CommCareHttpResponse(statusCode, headers.toArray(new Header[0]), IOUtils.toByteArray(getClass().getResourceAsStream(jsonDump)));
    }

    private void assertForm(CommCareFormInstance actualFormInstance, String[] expectedValuesOfForm, String formName, String expectedExtraDataValue) {
        assertEquals(actualFormInstance.formName(), formName);

        Map<String,String> data = actualFormInstance.fields();

        assertEquals(2, data.size());
        assertThat(data.get("FieldInOutput"), is(expectedValuesOfForm[0]));
        assertThat(data.get("AnotherFieldInOutput"), is(expectedValuesOfForm[1]));
        assertThat(actualFormInstance.extraData().get("typeOfExtraData").get("extraDataFieldName"), is(expectedExtraDataValue));
    }
}
