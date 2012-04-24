package org.motechproject.casexml.service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.casexml.service.response.CaseResponse;
import org.motechproject.casexml.service.response.converter.ExceptionConverter;

public class ResponseMessageBuilder {
    public ResponseMessageBuilder() {
    }

    String createResponseMessage(CaseException exception) {
        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

        CaseResponse response = new CaseResponse();
        response.setStatus("Failure");
        response.setMessage(exception.getMessage());
        response.add(exception);

        xstream.alias("response", CaseResponse.class);
        xstream.alias("error", exception.getClass());
        xstream.omitField(exception.getClass(),"stackTrace");

        xstream.registerConverter(new ExceptionConverter());

        return xstream.toXML(response);

    }

    public String messageForRuntimeException() {
        return "An unexpected error occured while processing .Please verify the message and try again";
    }
}