package org.motechproject.calllog.mapper;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.calllog.builder.CallLogBuilder;
import org.motechproject.calllog.domain.CallLog;
import org.motechproject.calllog.request.CallLogRequest;
import org.motechproject.calllog.request.OutboundDetails;
import org.motechproject.calllog.util.DateTimeConverter;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CallLogMapperTest {
    @Test
    public void shouldMapCallLogRequestToCallLog() {
        String endTime = new DateTimeConverter(new DateTime()).value();
        String startTime = new DateTimeConverter(new DateTime()).value();
        String attemptTime = new DateTimeConverter(new DateTime()).value();

        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setCallId("callId");
        callLogRequest.setDisposition("disposition");
        callLogRequest.setEndTime(endTime);
        callLogRequest.setStartTime(startTime);
        callLogRequest.setPhoneNumber("1234567890");
        callLogRequest.setErrorMessage("errorMessage");

        OutboundDetails outboundDetails = new OutboundDetails();
        outboundDetails.setAttemptTime(attemptTime.toString());
        outboundDetails.setAttempt("3");
        outboundDetails.setCallType("patientAlerts");
        outboundDetails.setRequestId("requestId");
        callLogRequest.setOutboundDetails(outboundDetails);

        HashMap<String, String> customData = new HashMap<>();
        customData.put("myData1", "myValue1");
        callLogRequest.setCustomData(customData);

        HashMap<String, String> callEvents = new HashMap<>();
        callEvents.put("myEvent1", "myValue1");
        callLogRequest.setCallEvents(callEvents);

        CallLog actualCallLog = new CallLogMapper().map(callLogRequest);

        CallLog expectedCallLog = new CallLogBuilder()
                .withDefaults()
                .withStartDateTime(DateTimeConverter.timestamp(startTime))
                .withEndDateTime(DateTimeConverter.timestamp(endTime))
                .withAttemptDateTime(DateTimeConverter.timestamp(attemptTime)).build();

        assertThat(actualCallLog, is(expectedCallLog));
    }

    @Test
    public void shouldNotMapOutboundDetailsIfItIsNotThere() {
        String endTime = new DateTimeConverter(new DateTime()).value();
        String startTime = new DateTimeConverter(new DateTime()).value();

        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setCallId("callId");
        callLogRequest.setDisposition("disposition");
        callLogRequest.setEndTime(endTime);
        callLogRequest.setStartTime(startTime);
        callLogRequest.setPhoneNumber("1234567890");
        callLogRequest.setErrorMessage("errorMessage");

        HashMap<String, String> customData = new HashMap<>();
        customData.put("myData1", "myValue1");
        callLogRequest.setCustomData(customData);

        HashMap<String, String> callEvents = new HashMap<>();
        callEvents.put("myEvent1", "myValue1");
        callLogRequest.setCallEvents(callEvents);

        CallLog actualCallLog = new CallLogMapper().map(callLogRequest);

        CallLog expectedCallLog = new CallLogBuilder()
                .withDefaults()
                .withNullOutboundDetails()
                .withStartDateTime(DateTimeConverter.timestamp(startTime))
                .withEndDateTime(DateTimeConverter.timestamp(endTime))
                .build();

        assertThat(actualCallLog, is(expectedCallLog));
    }
}
