package org.motechproject.calllog.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.calllog.builder.CallLogBuilder;
import org.motechproject.calllog.domain.CallLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationCallLogContext.xml")
public class GenericCallLogRepositoryIT{

    @Autowired
    GenericCallLogRepository callLogRepository;

    @Test
    public void shouldCreateCallLog() {
        CallLog callLog = new CallLogBuilder()
                .withDefaults()
                .build();

        callLogRepository.save(callLog);

        assertNotNull(callLog.getId());
        CallLog callLogFromDB = callLogRepository.findOne(callLog.getId());
        assertThat(callLogFromDB, is(callLog));
    }

    @Test
    public void shouldUpdateCallLog() {
        CallLog callLog = new CallLogBuilder()
                .withDefaults()
                .build();

        callLogRepository.save(callLog);
        CallLog callLogFromDB = callLogRepository.findOne(callLog.getId());

        callLogFromDB.setCallType("newType");

        Map<String, String> customData = new HashMap<>();
        customData.put("key2", "value22");
        callLogFromDB.setCustomData(customData);

        callLogRepository.save(callLogFromDB);

        assertThat(callLogRepository.findOne(callLog.getId()).getCallType(), is("newType"));
        assertThat(callLogRepository.findOne(callLog.getId()).getCustomData().get("key2"), is("value22"));
    }

    @Test
    public void shouldUpdateCallLogWithNullCustomFields() {

        CallLog callLog = new CallLogBuilder()
                .withDefaults()
                .build();

        callLogRepository.save(callLog);
        CallLog callLogFromDB = callLogRepository.findOne(callLog.getId());

        callLogFromDB.setCustomData(null);
        callLogRepository.save(callLogFromDB);

        callLog.setCustomData(new HashMap<String, String>());
        assertThat(callLogRepository.findOne(callLog.getId()), is(callLog));
    }

    @After
    public void tearDown() {
        callLogRepository.deleteAll();
    }
}
