package org.motechproject.casexml.service.response;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-caseService.xml")
public class ResponseMessageBuilderTest {

    public static String newline = System.getProperty("line.separator");

    @Autowired
    private VelocityEngine velocityEngine;

    @Test
    public void shouldCreateErrorResponseForCaseExceptionWithErrorMessages(){
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder(velocityEngine);

        Map<String,String> errorMessages = new HashMap<String,String>();
        errorMessages.put("520","Address Validation failed");
        errorMessages.put("521","TBIxd Validation failed");
        CaseException exception = new CaseException("Validation failed", HttpStatus.BAD_REQUEST,errorMessages);

        String responseMessage = messageBuilder.createResponseMessage(exception);

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">Validation failed</message>" + newline +
                "    <errors>" + newline +
                "        <error>" + newline +
                "            <code>521</code>" + newline +
                "            <message>TBIxd Validation failed</message>" + newline +
                "        </error>" + newline +
                "        <error>" + newline +
                "            <code>520</code>" + newline +
                "            <message>Address Validation failed</message>" + newline +
                "        </error>" + newline +
                "    </errors>" + newline +
                "</OpenRosaResponse>";
        assertEquals(expectedResponse, responseMessage);
    }

    @Test
    public void shouldCreateErrorResponseForCaseExceptionWithoutErrorMessages(){
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder(velocityEngine);

        CaseException exception = new CaseException("Validation failed", HttpStatus.BAD_REQUEST, null);

        String responseMessage = messageBuilder.createResponseMessage(exception);

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">Validation failed</message>" + newline +
                "</OpenRosaResponse>";
        assertEquals(expectedResponse,responseMessage);
    }

    @Test
    public void shouldCreateValidSuccessResponse() {
        ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder(velocityEngine);
        String successResponse = responseMessageBuilder.messageForSuccess();

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_success\">Request successfully processed.</message>" + newline +
                "</OpenRosaResponse>";
        assertEquals(expectedResponse, successResponse);
    }

    @Test
    public void shouldCreateValidErrorResponseForRuntimeException() {
        ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder(velocityEngine);
        String errorResponse = responseMessageBuilder.messageForRuntimeException();

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">An unexpected exception occurred while processing. Please verify the message and try again.</message>" + newline +
                "</OpenRosaResponse>";
        assertEquals(expectedResponse, errorResponse);
    }
}
