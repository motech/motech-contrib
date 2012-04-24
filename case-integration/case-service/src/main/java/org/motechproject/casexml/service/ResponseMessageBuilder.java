package org.motechproject.casexml.service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.motechproject.casexml.service.exception.CaseValidationException;
import org.motechproject.casexml.service.response.CaseResponse;
import org.motechproject.casexml.service.response.converter.ExceptionConverter;

public class ResponseMessageBuilder {
    public ResponseMessageBuilder() {
    }

    String createResponseMessage(CaseValidationException validationException) {
        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

        CaseResponse response = new CaseResponse();
        response.setStatus("Failure");
        response.setMessage(validationException.getMessage());
        response.add(validationException);

        xstream.alias("response", CaseResponse.class);
        xstream.alias("error", CaseValidationException.class);

        xstream.registerConverter(new ExceptionConverter());

        return xstream.toXML(response);

    }

    public String messageForRuntimeException() {
        return "An unexpected error occured while processing .Please verify the message and try again";
    }
}