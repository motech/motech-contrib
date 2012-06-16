package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.listener.event.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.ibm.icu.impl.Assert.fail;
import static java.util.Arrays.asList;
import static org.ei.commcare.api.domain.CommCareFormContent.FORM_ID_FIELD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommCareFormSubmissionRouterTest {
    public static final String FORM_ID = "FORM-ID-1";
    @Mock
    FakeDrishtiController drishtiController;
    @Mock
    AuditorRegistrar auditor;

    private CommCareFormSubmissionRouter commCareFormSubmissionRouter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

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

    @Test(expected = FormDispatchFailedException.class)
    public void shouldFailIfTheInvokedMethodThrowsAnException() throws Exception {
        CommCareFormSubmissionRouter dispatcher = new CommCareFormSubmissionRouter(auditor);
        dispatcher.registerForDispatch(new FakeDrishtiController());

        MotechEvent event = eventFor(form("methodWhichThrowsAnException", asList("name", "age"), asList("Mom", "23")));

        dispatcher.handle(event);
    }

    @Test
    public void shouldDispatchToOtherMethodsEvenIfOneDispatchFails() throws Exception {
        CommCareFormSubmissionRouter dispatcher = new CommCareFormSubmissionRouter(auditor);
        dispatcher.registerForDispatch(drishtiController);

        drishtiController.methodWhichThrowsAnException(any(FakeMotherRegistrationRequest.class));
        doThrow(new RuntimeException("boo")).when(drishtiController).methodWhichThrowsAnException(any(FakeMotherRegistrationRequest.class));

        MotechEvent event = eventFor(
                form("registerMother", asList("name", "age"), asList("Mom1", "23")),
                form("methodWhichThrowsAnException", asList("name", "age"), asList("Mom", "23")),
                form("methodWhichThrowsAnException", asList("name", "age"), asList("Mom", "23")),
                form("registerMother", asList("name", "age"), asList("Mom1", "23")));

        try {
            dispatcher.handle(event);
            fail("Should have thrown an exception");
        } catch (FormDispatchFailedException e) {
            assertThat(e.innerExceptions().size(), is(2));
            assertThat(e.innerExceptions().get(0).getMessage(), is("Failed during dispatch. Info: Form ID: FORM-ID-1, Method: methodWhichThrowsAnException, Parameter JSON: {\"age\":\"23\",\"name\":\"Mom\"}"));
            assertThat(e.innerExceptions().get(0).getCause().getMessage(), is("boo"));
            assertThat(e.innerExceptions().get(1).getMessage(), is("Failed during dispatch. Info: Form ID: FORM-ID-1, Method: methodWhichThrowsAnException, Parameter JSON: {\"age\":\"23\",\"name\":\"Mom\"}"));
            assertThat(e.innerExceptions().get(1).getCause().getMessage(), is("boo"));
        }

        verify(drishtiController, times(2)).registerMother(new FakeMotherRegistrationRequest("Mom1", 23));
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
        commCareFormSubmissionRouter.handle(eventFor(form("ancVisit", asList("something"), asList("3"))));

        verify(auditor).auditFormSubmission(FORM_ID, "ancVisit", "{\"something\":\"3\"}");
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