package org.motechproject.calllog.request;

import lombok.Data;
import org.motechproject.calllog.validation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class OutboundDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1)
    private String callType;

    @NotNull
    @Size(min = 1)
    private String requestId;

    @DateTimeFormat
    private String attemptTime;

    @NotNull
    @Size(min = 1)
    private String attempt;
}
