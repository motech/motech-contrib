package org.ei.commcare.listener.event;

import java.util.Map;

public class FakeDrishtiController {
    public void registerMother(FakeMotherRegistrationRequest request) {
    }

    public void ancVisit(FakeANCVisitRequestWithoutADefaultConstructor request) {
    }

    public void methodWithoutArguments() {
    }

    public void methodWithTwoArguments(FakeMotherRegistrationRequest request, Map<String, Map<String, String>> extraData) {
    }

    public void methodWithThreeArguments(int a, int b, int c) {
    }

    public void methodWhichThrowsAnException(FakeMotherRegistrationRequest request) {
        throw new RuntimeException("Boo");
    }

    public void methodWithArgumentHavingADate(FakeRequestWithDate fakeRequestWithDate) {
    }
}
