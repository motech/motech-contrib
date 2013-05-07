package org.motechproject.calllog.service;

import org.motechproject.calllog.domain.CallLog;
import org.motechproject.calllog.mapper.CallLogMapper;
import org.motechproject.calllog.repository.GenericCallLogRepository;
import org.motechproject.calllog.request.CallLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallLogService {

    private CallLogMapper callLogMapper;
    private GenericCallLogRepository genericCallLogRepository;

    @Autowired
    public CallLogService(CallLogMapper callLogMapper, GenericCallLogRepository genericCallLogRepository) {
        this.callLogMapper = callLogMapper;
        this.genericCallLogRepository = genericCallLogRepository;
    }

    public void add(CallLogRequest callLogRequest) {
        CallLog callLog = callLogMapper.map(callLogRequest);
        genericCallLogRepository.save(callLog);
    }
}
