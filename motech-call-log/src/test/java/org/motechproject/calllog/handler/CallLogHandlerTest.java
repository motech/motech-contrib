package org.motechproject.calllog.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.validation.validator.BeanValidator;
import org.motechproject.calllog.request.CallLogRequest;
import org.motechproject.calllog.service.CallLogService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.calllog.handler.EventKeys.CALL_LOG_RECEIVED;

public class CallLogHandlerTest {

    private CallLogHandler callLogHandler;

    @Mock
    private CallLogService callLogService;
    @Mock
    private BeanValidator beanValidator;

    @Before
    public void setUp() {
        initMocks(this);
        callLogHandler = new CallLogHandler(callLogService, beanValidator);
    }

    @Test
    public void shouldHandleTheCallLogReceivedRequest(){
        Map<String, Object> params = new HashMap<>();
        CallLogRequest callLogRequest = mock(CallLogRequest.class);

        params.put("0", callLogRequest);
        MotechEvent motechEvent = new MotechEvent(CALL_LOG_RECEIVED, params);

        callLogHandler.handleCallLogReceived(motechEvent);

        verify(callLogService).add(callLogRequest);
    }
}
