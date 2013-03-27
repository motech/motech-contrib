package org.motechproject.calllog.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.calllog.controller.CallLogController;
import org.motechproject.calllog.domain.CallLog;
import org.motechproject.calllog.domain.DispositionType;
import org.motechproject.calllog.repository.GenericCallLogRepository;
import org.motechproject.calllog.request.CallLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationCallLogContext.xml")
public class CallLogControllerIT {

    private CallLogController callLogController;

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    private EventContext eventContext;
    @Autowired
    private GenericCallLogRepository callLogRepository;

    @Before
    public void setUp() {
        callLogController = new CallLogController(eventContext);
    }

    @Test
    public void shouldHandleCallLogRequest() throws Exception {
        String callId = "callId";
        String phoneNumber = "1234567890";

        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setCallId(callId);
        callLogRequest.setPhoneNumber(phoneNumber);
        callLogRequest.setDisposition(DispositionType.FAILED.name());
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

        new TimedRunner(20, 1000) {
            @Override
            protected void run() {
                List<CallLog> allCallLogs = callLogRepository.findAll();
                assertEquals(allCallLogs.size(), 1);
            }
        }.executeWithTimeout();

        List<CallLog> allCallLogs = callLogRepository.findAll();
        assertEquals(allCallLogs.size(), 1);
        assertThat(allCallLogs.get(0).getCallId(), is(callId));
        assertThat(allCallLogs.get(0).getPhoneNumber(), is(phoneNumber));
    }

    @After
    public void tearDown() throws Exception {
        callLogRepository.deleteAll();
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