package org.motechproject.calllog.controller;

import org.apache.log4j.Logger;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.calllog.request.CallLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.motechproject.calllog.handler.EventKeys.CALL_LOG_RECEIVED;

@Controller
@RequestMapping(value = "/callLog")
public class CallLogController {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private EventContext eventContext;

    @Autowired
    public CallLogController(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void log(@RequestBody CallLogRequest callLogRequest) {
        eventContext.send(CALL_LOG_RECEIVED, callLogRequest);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(RuntimeException ex) {
        logger.error("Error occurred", ex);
        throw ex;
    }
}
