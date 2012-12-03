package org.motechproject.web.message.converters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomJaxb2RootElementHttpMessageConverterTest {

    @Mock
    private HttpInputMessage httpInputMessage;

    @Mock
    private HttpOutputMessage httpOutputMessage;

    @Mock
    private HttpHeaders httpOutputHeaders;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() {
        initMocks(this);
        when(httpOutputMessage.getHeaders()).thenReturn(httpOutputHeaders);
    }

    @Test
    public void shouldNotSetHeaderWhileSerializing() throws IOException {
        Request request = new Request("myname", 42, new RequestInformation("mymessage"), Arrays.asList("nickname1", "nickname2"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        new CustomJaxb2RootElementHttpMessageConverter().write(request, null, httpOutputMessage);

        assertEquals("<request>\n" +
                "    <name>myname</name>\n" +
                "    <age>42</age>\n" +
                "    <information>\n" +
                "        <message>mymessage</message>\n" +
                "    </information>\n" +
                "    <nicknames>\n" +
                "        <nickname>nickname1</nickname>\n" +
                "        <nickname>nickname2</nickname>\n" +
                "    </nicknames>\n" +
                "</request>", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.APPLICATION_XML);
    }


    @Test
    public void shouldSetHeaderWhileSerializing() throws IOException {
        Request request = new Request("myname", 42, new RequestInformation(null), new ArrayList<String>());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        CustomJaxb2RootElementHttpMessageConverter converter = new CustomJaxb2RootElementHttpMessageConverter();
        converter.setIncludeHeader(true);
        converter.write(request, null, httpOutputMessage);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<request>\n" +
                "    <name>myname</name>\n" +
                "    <age>42</age>\n" +
                "    <information/>\n" +
                "    <nicknames/>\n" +
                "</request>", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.APPLICATION_XML);
    }

    @Test
    public void shouldNotFormatWhileSerializing() throws IOException {
        Request request = new Request("myname", 42, null, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        CustomJaxb2RootElementHttpMessageConverter converter = new CustomJaxb2RootElementHttpMessageConverter();
        converter.setFormattedOutput(false);
        converter.write(request, null, httpOutputMessage);

        assertEquals("<request><name>myname</name><age>42</age></request>", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.APPLICATION_XML);
    }

    @Test
    public void shouldNotThrowExceptionIfExtraXmlElementIsFoundWhileDeserializing() throws IOException {
        CustomJaxb2RootElementHttpMessageConverter converter = new CustomJaxb2RootElementHttpMessageConverter();
        converter.setIgnoreMissingElements(true);
        String input = "<request>\n" +
                "    <name>myname</name>\n" +
                "    <age>42</age>\n" +
                "    <information>\n" +
                "        <message>mymessage</message>\n" +
                "    </information>\n" +
                "    <myinformation/>\n" +
                "    <nicknames>\n" +
                "        <nickname>nickname1</nickname>\n" +
                "        <nickname>nickname2</nickname>\n" +
                "    </nicknames>\n" +
                "</request>";

        when(httpInputMessage.getBody()).thenReturn(new ByteArrayInputStream(input.getBytes()));

        Request actualRequest = (Request) converter.read(Request.class, httpInputMessage);
        Request expectedRequest = new Request("myname", 42, new RequestInformation("mymessage"), Arrays.asList("nickname1", "nickname2"));

       assertEquals(expectedRequest, actualRequest);
    }

    @Test
    public void shouldThrowExceptionIfExtraXmlElementIsFoundWhileDeserializing() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("");

        CustomJaxb2RootElementHttpMessageConverter converter = new CustomJaxb2RootElementHttpMessageConverter();
        String input = "<request>\n" +
                "    <name>myname</name>\n" +
                "    <age>42</age>\n" +
                "    <information>\n" +
                "        <message>mymessage</message>\n" +
                "    </information>\n" +
                "    <myinformation/>\n" +
                "    <nicknames>\n" +
                "        <nickname>nickname1</nickname>\n" +
                "        <nickname>nickname2</nickname>\n" +
                "    </nicknames>\n" +
                "</request>";

        when(httpInputMessage.getBody()).thenReturn(new ByteArrayInputStream(input.getBytes()));
        converter.read(Request.class, httpInputMessage);
    }
}
