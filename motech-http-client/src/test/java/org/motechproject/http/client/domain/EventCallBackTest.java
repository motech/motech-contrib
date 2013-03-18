package org.motechproject.http.client.domain;

import org.junit.Test;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class EventCallBackTest {

    @Test
    public void shouldCreateMotechEvent() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("pageNo", 1);
        parameters.put("someParam", "someValue");
        String subject = "test";

        MotechEvent expectedMotechEvent = new MotechEvent(subject, parameters);

        EventCallBack eventCallBack = new EventCallBack(subject, parameters);

        assertEquals(expectedMotechEvent, eventCallBack.getCallBackEvent());
    }
}
