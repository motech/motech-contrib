package org.motechproject.casexml.service.response;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.motechproject.casexml.exception.CaseParserException;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.casexml.service.response.converter.ExceptionConverter;

public class ResponseMessageBuilder {
    XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

    public String createResponseMessage(CaseException exception) {
        CaseResponse response = new CaseResponse();
        response.setStatus("Failure");
        response.setMessage(exception.getMessage());
        response.setException(exception);

        xstream.alias("response", CaseResponse.class);
        xstream.alias("errors", exception.getClass());
        xstream.omitField(exception.getClass(),"stackTrace");
        xstream.omitField(exception.getClass(),"errorMessages");

        xstream.registerConverter(new ExceptionConverter());

        return xstream.toXML(response);
    }

    public String createResponseMessage(CaseParserException exception){
        return responseWithMessage("An unexpected exception occurred while while trying to parse caseXml", "Failure");
    }

    public String messageForRuntimeException() {
        return responseWithMessage("An unexpected exception occurred while processing .Please verify the message and try again", "Failure");
    }

    public String messageForSuccess() {
        return responseWithMessage("Request successfully processed", "Success");
    }

    private String responseWithMessage(String message, String status) {
        CaseResponse response = new CaseResponse();
        response.setStatus(status);
        response.setMessage(message);

        xstream.alias("response", CaseResponse.class);
        xstream.omitField(CaseResponse.class,"errors");

        return xstream.toXML(response);
    }
}