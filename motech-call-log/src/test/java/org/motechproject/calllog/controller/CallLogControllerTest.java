package org.motechproject.calllog.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.calllog.request.CallLogRequest;

import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.calllog.handler.EventKeys.CALL_LOG_RECEIVED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class CallLogControllerTest {

    private CallLogController callLogController;

    @Mock
    private EventContext eventcontext;

    @Before
    public void setUp() {
        initMocks(this);
        callLogController = new CallLogController(eventcontext);
    }

    @Test
    public void shouldHandleCallLogRequest() throws Exception {
        CallLogRequest callLogRequest = new CallLogRequest();
        String callId = "callId";
        callLogRequest.setCallId(callId);
        callLogRequest.setPhoneNumber("1234567890");
        HashMap<String, String> customData = new HashMap<>();
        String key = "patientId";
        String value = "12345";
        customData.put(key, value);
        callLogRequest.setCustomData(customData);

        String requestJSON = getJSON(callLogRequest);
        standaloneSetup(callLogController)
                .build()
                .perform(
                        post("/callLog/log")
                        .body(requestJSON.getBytes())
                        .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk());

        ArgumentCaptor<CallLogRequest> captor = ArgumentCaptor.forClass(CallLogRequest.class);
        verify(eventcontext).send(eq(CALL_LOG_RECEIVED), captor.capture());
        CallLogRequest actualCallLogRequest = captor.getValue();

        assertThat(actualCallLogRequest.getCallId(), is(callId));
        assertThat(actualCallLogRequest.getCustomData().get(key), is(value));
    }

    protected String getJSON(Object object) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writer().writeValueAsString(object);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}