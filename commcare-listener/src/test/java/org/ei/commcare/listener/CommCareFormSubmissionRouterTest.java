package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.listener.event.*;
import org.ei.drishti.common.audit.Auditor;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.Arrays.asList;
import static org.ei.commcare.api.domain.CommCareFormContent.FORM_ID_FIELD;
import static org.ei.drishti.common.audit.AuditMessageType.FORM_SUBMISSION;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommCareFormSubmissionRouterTest {
    public static final String FORM_ID = "FORM-ID-1";
    @Mock
    FakeDrishtiController drishtiController;
    @Mock
    Auditor auditor;
    @Mock
    private Auditor.AuditMessageBuilder messageBuilder;

    private CommCareFormSubmissionRouter commCareFormSubmissionRouter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(auditor.audit(FORM_SUBMISSION)).thenReturn(messageBuilder);
        when(messageBuilder.with(any(String.class), any(String.class))).thenReturn(messageBuilder);

        commCareFormSubmissionRouter = new CommCareFormSubmissionRouter(auditor);
        commCareFormSubmissionRouter.registerForDispatch(drishtiController);
    }

    @Test
    public void shouldDispatchToMethodSpecifiedByFormNameWithAnArgumentFromFormDataWhenThereIsOnlyOneFormInstance() throws Exception {
        MotechEvent event = eventFor(form("registerMother", asList("name", "age"), asList("Mom", "23")));

        commCareFormSubmissionRouter.handle(event);

        verify(drishtiController).registerMother(new FakeMotherRegistrationRequest("Mom", 23));
    }

    @Test
    public void shouldDispatchToMethodSpecifiedByFormNameWithAnArgumentFromFormDataWhenThereIsMultipleFormInstance() throws Exception {
        CommCareFormInstance firstEvent = form("registerMother", asList("name", "age"), asList("Mom1", "23"));
        CommCareFormInstance secondEvent = form("registerMother", asList("name", "age"), asList("Mom2", "24"));

        commCareFormSubmissionRouter.handle(eventFor(firstEvent, secondEvent));

        verify(drishtiController).registerMother(new FakeMotherRegistrationRequest("Mom1", 23));
        verify(drishtiController).registerMother(new FakeMotherRegistrationRequest("Mom2", 24));
    }

    @Test
    public void shouldBeAbleToCreateArgumentsWithDateInThemWhenDateIsInYYYYMMDDFormat() throws Exception {
        MotechEvent event = eventFor(form("methodWithArgumentHavingADate", asList("date"), asList("2000-03-23")));

        commCareFormSubmissionRouter.handle(event);

        verify(drishtiController).methodWithArgumentHavingADate(new FakeRequestWithDate(new DateTime(2000, 3, 23, 0, 0).toDate()));
    }

    @Test
    public void shouldNotFailIfMethodForDispatchIsNotFound() throws Exception {
        MotechEvent event = eventFor(form("someMethodWhichDoesNotExist", asList("name", "age"), asList("Mom", "23")));

        commCareFormSubmissionRouter.handle(event);

        verifyZeroInteractions(drishtiController);
    }

    @Test(expected = InvocationTargetException.class)
    public void shouldFailIfTheInvokedMethodThrowsAnException() throws Exception {
        CommCareFormSubmissionRouter dispatcher = new CommCareFormSubmissionRouter(auditor);
        dispatcher.registerForDispatch(new FakeDrishtiController());

        MotechEvent event = eventFor(form("methodWhichThrowsAnException", asList("name", "age"), asList("Mom", "23")));

        dispatcher.handle(event);
    }

    @Test
    public void shouldNotCallAMethodWithValidNameWithZeroArguments() throws Exception {
        MotechEvent event = eventFor(form("methodWithoutArguments", new ArrayList<String>(), new ArrayList<String>()));

        commCareFormSubmissionRouter.handle(event);

        verifyZeroInteractions(drishtiController);
    }

    @Test
    public void shouldNotCallAMethodWithValidNameWithTwoArguments() throws Exception {
        MotechEvent event = eventFor(form("methodWithTwoArguments", new ArrayList<String>(), new ArrayList<String>()));

        commCareFormSubmissionRouter.handle(event);

        verifyZeroInteractions(drishtiController);
    }

    @Test
    public void shouldDispatchEvenIfParameterClassDoesNotHaveADefaultConstructor() throws Exception {
        commCareFormSubmissionRouter.handle(eventFor(form("ancVisit", asList("something"), asList("3"))));

        verify(drishtiController).ancVisit(new FakeANCVisitRequestWithoutADefaultConstructor(3));
    }

    @Test
    public void shouldNotFailIfNoObjectToRouteToHasBeenSetup() throws Exception {
        CommCareFormSubmissionRouter dispatcher = new CommCareFormSubmissionRouter(auditor);

        dispatcher.registerForDispatch(null);

        dispatcher.handle(eventFor(form("registerMother", new ArrayList<String>(), new ArrayList<String>())));
    }

    @Test
    public void shouldAuditWhenAFormSubmissionSuccessfullyGoesThroughToTheController() throws Exception {
        when(auditor.audit(FORM_SUBMISSION)).thenReturn(messageBuilder);

        commCareFormSubmissionRouter.handle(eventFor(form("ancVisit", asList("something"), asList("3"))));

        verify(messageBuilder).with("formId", FORM_ID);
        verify(messageBuilder).with("formType", "ancVisit");
        verify(messageBuilder).with("formData", "{\"something\":\"3\"}");
    }

    private MotechEvent eventFor(CommCareFormInstance... events) {
        return new CommCareFormEvent(Arrays.asList(events)).toMotechEvent();
    }

    private CommCareFormContent content(List<String> headers, List<String> values) {
        ArrayList<String> headersWithID = new ArrayList<String>(headers);
        headersWithID.add(FORM_ID_FIELD);

        ArrayList<String> valuesWithID = new ArrayList<String>(values);
        valuesWithID.add(FORM_ID);

        return new CommCareFormContent(headersWithID, valuesWithID);
    }

    private CommCareFormInstance form(String formName, List<String> fieldNames, List<String> values) {
        List<String> commCareFieldNames = generateRandomNames(fieldNames.size());

        CommCareFormContent content = content(commCareFieldNames, values);
        CommCareFormBuilder formBuilder = new CommCareFormBuilder().withName(formName).withContent(content);

        for (int i = 0; i < fieldNames.size(); i++) {
            formBuilder.withMapping(commCareFieldNames.get(i), fieldNames.get(i));
        }

        return formBuilder.build();
    }

    private List<String> generateRandomNames(int size) {
        ArrayList<String> values = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            values.add(String.valueOf(new Random().nextFloat()));
        }
        return values;
    }
}
