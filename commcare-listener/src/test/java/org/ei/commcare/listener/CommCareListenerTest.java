package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.service.CommCareModuleImportService;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.ei.commcare.api.domain.CommCareFormContent.FORM_ID_FIELD;
import static org.ei.commcare.listener.event.CommCareFormEvent.EVENT_SUBJECT;
import static org.ei.commcare.listener.event.CommCareFormEvent.FORM_INSTANCES_PARAMETER;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

public class CommCareListenerTest {
    @Mock
    CommCareModuleImportService moduleImportService;

    @Mock
    OutboundEventGateway outboundEventGateway;

    public static final String FORM_ID = "FORM-ID-1";
    private CommCareListener listener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        listener = new CommCareListener(outboundEventGateway, moduleImportService);
    }

    @Test
    public void shouldSendAndEventForAModule() throws Exception {
        CommCareFormInstance formInstance = form().withName("FormName").withContent(content(asList("something"), asList("something-else"))).build();
        List<List<CommCareFormInstance>> instances = asList(asList(formInstance));
        when(moduleImportService.fetchFormsForAllModules()).thenReturn(instances);

        listener.fetchFromServer();

        verify(outboundEventGateway).sendEventMessage(eventWhichMatches(instances.get(0)));
        verifyNoMoreInteractions(outboundEventGateway);
    }

    @Test
    public void shouldSendOneEventForEachModule() throws Exception {
        CommCareFormInstance formInstanceForFirstModule = form().withName("FormName 1").withContent(content(asList("something"), asList("something-else"))).build();
        CommCareFormInstance formInstanceForSecondModule = form().withName("FormName 2").withContent(content(asList("something"), asList("something-else"))).build();
        List<List<CommCareFormInstance>> instances = asList(asList(formInstanceForFirstModule), asList(formInstanceForSecondModule));
        when(moduleImportService.fetchFormsForAllModules()).thenReturn(instances);

        listener.fetchFromServer();

        verify(outboundEventGateway).sendEventMessage(eventWhichMatches(instances.get(0)));
        verify(outboundEventGateway).sendEventMessage(eventWhichMatches(instances.get(1)));
        verifyNoMoreInteractions(outboundEventGateway);
    }

    private CommCareFormContent content(List<String> headers, List<String> values) {
        ArrayList<String> headersWithID = new ArrayList<String>(headers);
        headersWithID.add(FORM_ID_FIELD);

        ArrayList<String> valuesWithID = new ArrayList<String>(values);
        valuesWithID.add(FORM_ID);

        return new CommCareFormContent(headersWithID, valuesWithID);
    }

    private CommCareFormBuilder form() {
        return new CommCareFormBuilder();
    }

    private MotechEvent eventWhichMatches(final List<CommCareFormInstance> commCareFormInstances) {
        return argThat(new ArgumentMatcher<MotechEvent>() {
            @Override
            public boolean matches(Object actualEvent) {
                MotechEvent event = (MotechEvent) actualEvent;

                return EVENT_SUBJECT.equals(event.getSubject()) && event.getParameters().get(FORM_INSTANCES_PARAMETER).equals(commCareFormInstances);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Subject: " + EVENT_SUBJECT + ", Forms: " + commCareFormInstances);
            }
        });
    }
}
