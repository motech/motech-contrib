package org.motechproject.calllog.handler;

import org.apache.commons.lang.StringUtils;
import org.motechproject.calllog.request.CallLogRequest;
import org.motechproject.calllog.service.CallLogService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallLogHandler {

    private CallLogService callLogService;
    private Validator validator;

    @Autowired
    public CallLogHandler(CallLogService callLogService,@Qualifier(value = "callLogValidator") Validator validator) {
        this.callLogService = callLogService;
        this.validator = validator;
    }

    @MotechListener(subjects = EventKeys.CALL_LOG_RECEIVED)
    public void handleCallLogReceived(MotechEvent motechEvent) {
        CallLogRequest callLogRequest = (CallLogRequest) motechEvent.getParameters().get("0");
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(callLogRequest, callLogRequest.getClass().getSimpleName());
        validator.validate(callLogRequest, result);
        if(result.hasErrors()) {
            throw new RuntimeException(constructErrorMessage(result));
        }
        callLogService.add(callLogRequest);
    }

    private String constructErrorMessage(Errors errors) {
        List<String> messages = new ArrayList();
        for (FieldError fieldError : errors.getFieldErrors()) {
            messages.add(String.format("field:%s:%s", fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return StringUtils.join(messages, ",");
    }
}
