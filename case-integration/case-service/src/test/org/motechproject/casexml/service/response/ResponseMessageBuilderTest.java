package org.motechproject.casexml.service.response;

import junit.framework.TestCase;
import org.junit.Test;
import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessageBuilderTest extends TestCase{
    
    @Test
    public void testShouldCreateErrorResponseForCaseException(){
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder();

        Map<String,String> errorMessages = new HashMap<String,String>();
        errorMessages.put("520","Address Validation failed");
        errorMessages.put("521","TBIxd Validation failed");
        CaseException exception = new CaseException("Validation failed", HttpStatus.BAD_REQUEST,errorMessages);

        String responseMessage = messageBuilder.createResponseMessage(exception);
        assertEquals(caseExceptionMessage(),responseMessage);
    }

    private String caseExceptionMessage() {
            return "<response>\n" +
                    "  <errors>\n" +
                    "    <error>\n" +
                    "      <code>521</code>\n" +
                    "      <message>TBIxd Validation failed</message>\n" +
                    "    </error>\n" +
                    "    <error>\n" +
                    "      <code>520</code>\n" +
                    "      <message>Address Validation failed</message>\n" +
                    "    </error>\n" +
                    "  </errors>\n" +
                    "  <status>Failure</status>\n" +
                    "  <message>Validation failed</message>\n" +
                    "</response>";
    }

}
