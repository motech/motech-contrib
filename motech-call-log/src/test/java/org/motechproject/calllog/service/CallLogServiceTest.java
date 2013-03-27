package org.motechproject.calllog.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.calllog.domain.CallLog;
import org.motechproject.calllog.mapper.CallLogMapper;
import org.motechproject.calllog.repository.GenericCallLogRepository;
import org.motechproject.calllog.request.CallLogRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CallLogServiceTest {

    private CallLogService callLogService;

    @Mock
    private GenericCallLogRepository genericCallLogRepository;
    @Mock
    private CallLogMapper callLogMapper;

    @Before
    public void setUp() {
        initMocks(this);
        callLogService = new CallLogService(callLogMapper, genericCallLogRepository);
    }

    @Test
    public void shouldAddCallLog() {
        CallLogRequest callLogRequest = new CallLogRequest();
        CallLog callLog = mock(CallLog.class);
        when(callLogMapper.map(callLogRequest)).thenReturn(callLog);

        callLogService.add(callLogRequest);

        verify(callLogMapper).map(callLogRequest);
        verify(genericCallLogRepository).save(callLog);
    }
}
