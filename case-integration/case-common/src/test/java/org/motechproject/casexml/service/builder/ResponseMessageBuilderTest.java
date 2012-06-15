package org.motechproject.casexml.service.builder;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import junit.framework.Assert;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.casexml.builder.ResponseMessageBuilder;
import org.motechproject.casexml.service.exception.CaseError;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-case.xml")
public class ResponseMessageBuilderTest {

    @Autowired
    private VelocityEngine velocityEngine;

    public static String newline = System.getProperty("line.separator");

    @Test
    public void shouldCreateErrorResponseForCaseExceptionWithErrorMessages() {
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder(velocityEngine);

        List<CaseError> errors = asList(new CaseError("520", "Address Validation failed"), new CaseError("521", "TBIxd Validation failed"));
        CaseException exception = new CaseException("Validation failed", HttpStatus.BAD_REQUEST, errors);

        String responseMessage = messageBuilder.createResponseMessage(exception);

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">Validation failed</message>" + newline +
                "    <errors>" + newline +
                "        <error>" + newline +
                "            <code>520</code>" + newline +
                "            <message>Address Validation failed</message>" + newline +
                "        </error>" + newline +
                "        <error>" + newline +
                "            <code>521</code>" + newline +
                "            <message>TBIxd Validation failed</message>" + newline +
                "        </error>" + newline +
                "    </errors>" + newline +
                "</OpenRosaResponse>";
        compareXmlString(expectedResponse, responseMessage);
    }

    @Test
    public void shouldCreateErrorResponseForCaseExceptionWithoutErrorMessages() {
        ResponseMessageBuilder messageBuilder = new ResponseMessageBuilder(velocityEngine);

        CaseException exception = new CaseException("Validation failed", HttpStatus.BAD_REQUEST, null);

        String responseMessage = messageBuilder.createResponseMessage(exception);

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">Validation failed</message>" + newline +
                "</OpenRosaResponse>";
        compareXmlString(expectedResponse, responseMessage);
    }

    @Test
    public void shouldCreateValidSuccessResponse() {
        ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder(velocityEngine);
        String successResponse = responseMessageBuilder.messageForSuccess();

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_success\">Request successfully processed.</message>" + newline +
                "</OpenRosaResponse>";
        compareXmlString(expectedResponse, successResponse);
    }

    @Test
    public void shouldCreateValidErrorResponseForRuntimeException() {
        ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder(velocityEngine);
        String errorResponse = responseMessageBuilder.messageForRuntimeException();

        String expectedResponse = "<OpenRosaResponse xmlns=\"http://openrosa.org/http/response\">" + newline +
                "    <message nature=\"submit_error\">An unexpected exception occurred while processing. Please verify the message and try again.</message>" + newline +
                "</OpenRosaResponse>";
        compareXmlString(expectedResponse, errorResponse);
    }

    private void compareXmlString(String expected, String actual) {
        expected = toString(documentFrom(expected));
        actual = toString(documentFrom(actual));
        Assert.assertEquals(expected, actual);
    }

    private Document documentFrom(String xmlDocument) {
        if (StringUtil.isNullOrEmpty(xmlDocument)) {
            throw new IllegalArgumentException();
        }

        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDocument));

        try {
            parser.parse(inputSource);
            return parser.getDocument();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String toString(Document doc) {

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
