package org.motechproject.calllog.builder;

import org.motechproject.calllog.domain.CallLog;
import org.motechproject.calllog.request.OutboundDetails;
import org.motechproject.calllog.util.DateTimeConverter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class CallLogBuilder {

    private CallLog callLog;

    public CallLogBuilder() {
        callLog = new CallLog();
    }

    public CallLog build() {
        return callLog;
    }

    public CallLogBuilder withDefaults() {
        callLog.setCallId("callId");
        callLog.setDisposition("disposition");
        callLog.setEndDateTime(new Timestamp(new Date().getTime()));
        callLog.setStartDateTime(new Timestamp(new Date().getTime()));
        callLog.setPhoneNumber("1234567890");
        callLog.setErrorMessage("errorMessage");

        callLog.setAttemptTime(new Timestamp(new Date().getTime()));
        callLog.setAttempt("3");
        callLog.setCallType("patientAlerts");
        callLog.setRequestId("requestId");

        HashMap<String, String> customData = new HashMap<>();
        customData.put("myData1", "myValue1");
        callLog.setCustomData(customData);

        HashMap<String, String> callEvents = new HashMap<>();
        callEvents.put("myEvent1", "myValue1");
        callLog.setCallEvents(callEvents);

        return this;
    }

    public CallLogBuilder withStartDateTime(Timestamp startDateTime) {
        callLog.setStartDateTime(startDateTime);
        return this;
    }

    public CallLogBuilder withOutboundDetails(OutboundDetails outboundDetails) {
        if(outboundDetails != null) {
            callLog.setAttemptTime(DateTimeConverter.timestamp(outboundDetails.getAttemptTime()));
            callLog.setAttempt(outboundDetails.getAttempt());
            callLog.setCallType(outboundDetails.getCallType());
            callLog.setRequestId(outboundDetails.getRequestId());
        }
        return this;
    }

    public CallLogBuilder withEndDateTime(Timestamp endDateTime) {
        callLog.setEndDateTime(endDateTime);
        return this;
    }

    public CallLogBuilder withAttemptDateTime(Timestamp attemptDatetime) {
        callLog.setAttemptTime(attemptDatetime);
        return this;
    }

    public CallLogBuilder withCustomData(String key, String value){
        if(callLog.getCustomData() == null){
            callLog.setCustomData(new HashMap<String, String>());
        }
        callLog.getCustomData().put(key, value);
        return this;
    }

    public CallLogBuilder withCallEvents(String key, String value){
        if(callLog.getCallEvents() == null){
            callLog.setCallEvents(new HashMap<String, String>());
        }
        callLog.getCallEvents().put(key, value);
        return this;
    }

    public CallLogBuilder withNullOutboundDetails() {
        callLog.setAttemptTime(null);
        callLog.setAttempt(null);
        callLog.setCallType(null);
        callLog.setRequestId(null);
        return this;
    }
}
