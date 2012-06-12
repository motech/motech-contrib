package org.ei.commcare.listener;

import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.listener.event.CommCareFormEvent;
import org.ei.commcare.listener.event.FakeDrishtiController;
import org.junit.Before;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;

import java.util.*;

import static org.ei.commcare.api.domain.CommCareFormContent.FORM_ID_FIELD;
import static org.mockito.MockitoAnnotations.initMocks;

public abstract class CommCareFormSubmissionRouterTestBase {
    public static final String FORM_ID = "FORM-ID-1";
    @Mock
    FakeDrishtiController drishtiController;
    @Mock
    AuditorRegistrar auditor;
    protected CommCareFormSubmissionRouter commCareFormSubmissionRouter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        commCareFormSubmissionRouter = new CommCareFormSubmissionRouter(auditor);
        commCareFormSubmissionRouter.registerForDispatch(drishtiController);
    }

    protected MotechEvent eventFor(CommCareFormInstance... events) {
        return new CommCareFormEvent(Arrays.asList(events)).toMotechEvent();
    }

    private CommCareFormContent content(List<String> headers, List<String> values, List<String> extraFieldNames, List<String> extraFieldValues) {
        ArrayList<String> headersWithID = new ArrayList<String>(headers);
        headersWithID.addAll(extraFieldNames);
        headersWithID.add(FORM_ID_FIELD);

        ArrayList<String> valuesWithID = new ArrayList<String>(values);
        valuesWithID.addAll(extraFieldValues);
        valuesWithID.add(FORM_ID);

        return new CommCareFormContent(headersWithID, valuesWithID);
    }

    protected CommCareFormInstance form(String formName, List<String> fieldNames, List<String> values) {
        return form(formName, fieldNames, values, new ArrayList<String>(), new ArrayList<String>());
    }

    protected CommCareFormInstance form(String formName, List<String> fieldNames, List<String> values, List<String> extraFieldNames, List<String> extraFieldValues) {
        List<String> commCareFieldNames = generateRandomNames(fieldNames.size());
        List<String> commCareExtraDataFieldNames = generateRandomNames(extraFieldNames.size());

        CommCareFormContent content = content(commCareFieldNames, values, commCareExtraDataFieldNames, extraFieldValues);
        CommCareFormBuilder formBuilder = new CommCareFormBuilder().withName(formName).withContent(content);

        for (int i = 0; i < fieldNames.size(); i++) {
            formBuilder.withMapping(commCareFieldNames.get(i), fieldNames.get(i));
        }

        for (int i = 0; i < extraFieldNames.size(); i++) {
            formBuilder.withExtraMapping(commCareExtraDataFieldNames.get(i), extraFieldNames.get(i));
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
