package org.motechproject.calllog.handler;

import org.apache.commons.lang.StringUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.validation.validator.BeanValidator;
import org.motechproject.calllog.request.CallLogRequest;
import org.motechproject.calllog.service.CallLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallLogHandler {

    private CallLogService callLogService;
    private BeanValidator beanValidator;

    @Autowired
    public CallLogHandler(CallLogService callLogService, BeanValidator beanValidator) {
        this.callLogService = callLogService;
        this.beanValidator = beanValidator;
    }

    @MotechListener(subjects = EventKeys.CALL_LOG_RECEIVED)
    public void handleCallLogReceived(MotechEvent motechEvent) {
        CallLogRequest callLogRequest = (CallLogRequest) motechEvent.getParameters().get("0");
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(callLogRequest, callLogRequest.getClass().getSimpleName());
        beanValidator.validate(callLogRequest, null, result);
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
