package org.ei.commcare.listener;

import org.ei.commcare.listener.event.FakeMotherRegistrationRequest;
import org.junit.Test;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

public class CommCareFormSubmissionRouterForFormInstancesWithExtraDataTest extends CommCareFormSubmissionRouterTestBase {
    @Test
    public void shouldCallAMethodWithValidNameWithTwoArgumentsSinceThereIsExtraData() throws Exception {
        MotechEvent event = eventFor(form("methodWithTwoArguments",
                asList("name", "age"), asList("Mom", "23"),
                asList("extraFieldFromCommCare"), asList("booValueInFormSubmission")));

        commCareFormSubmissionRouter.handle(event);

        Map<String, Map<String, String>> expectedExtraData = new HashMap<>();
        expectedExtraData.put("typeOfExtraData", new HashMap<String, String>());
        expectedExtraData.get("typeOfExtraData").put("extraFieldFromCommCare", "booValueInFormSubmission");
        verify(drishtiController).methodWithTwoArguments(new FakeMotherRegistrationRequest("Mom", 23), expectedExtraData);
    }
}
