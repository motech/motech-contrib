package org.motechproject.web.message.converters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomMappingJacksonHttpMessageConverterTest {

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
    public void shouldIgnoreNullPropertiesWhileSerializing() throws IOException {
        Request request = new Request(null, 42, new RequestInformation("mymessage"), Arrays.asList("nickname1", "nickname2"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        new CustomMappingJacksonHttpMessageConverter().write(request, null, httpOutputMessage);

        assertEquals("{\"age\":42,\"information\":{\"message\":\"mymessage\"},\"nicknames\":[\"nickname1\",\"nickname2\"]}", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
    }

    @Test
    public void shouldIncludeNullPropertiesWhileSerializing() throws IOException {
        Request request = new Request(null, 42, new RequestInformation("mymessage"), Arrays.asList("nickname1", "nickname2"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        CustomMappingJacksonHttpMessageConverter converter = new CustomMappingJacksonHttpMessageConverter("ALWAYS");
        converter.write(request, null, httpOutputMessage);

        assertEquals("{\"name\":null,\"age\":42,\"information\":{\"message\":\"mymessage\"},\"nicknames\":[\"nickname1\",\"nickname2\"]}", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
    }

    @Test
    public void beansShouldBeAbleToOverrideInclusions() throws IOException {
        Request request = new Request(null, 42, new RequestInformation(null), Arrays.asList("nickname1", "nickname2"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(httpOutputMessage.getBody()).thenReturn(outputStream);

        CustomMappingJacksonHttpMessageConverter converter = new CustomMappingJacksonHttpMessageConverter("ALWAYS");
        converter.write(request, null, httpOutputMessage);

        assertEquals("{\"name\":null,\"age\":42,\"information\":{},\"nicknames\":[\"nickname1\",\"nickname2\"]}", outputStream.toString().trim());

        verify(httpOutputHeaders).setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
    }
}
