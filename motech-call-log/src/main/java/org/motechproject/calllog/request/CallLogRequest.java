package org.motechproject.calllog.request;

import lombok.Data;
import org.motechproject.calllog.domain.DispositionType;
import org.motechproject.calllog.validation.DateTimeFormat;
import org.motechproject.calllog.validation.Enumeration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

@Data
public class CallLogRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1)
    private String callId;

    @NotNull
    @Size(min = 1)
    private String phoneNumber;

    @DateTimeFormat(allowBlank = true)
    private String startTime;

    @DateTimeFormat(allowBlank = true)
    private String endTime;

    @NotNull
    @Size(min = 1)
    @Enumeration(value = DispositionType.class)
    private String disposition;

    private String errorMessage;

    @Valid
    @NotNull
    private OutboundDetails outboundDetails;

    private Map<String, String> callEvents;

    private Map<String, String> customData;
}
