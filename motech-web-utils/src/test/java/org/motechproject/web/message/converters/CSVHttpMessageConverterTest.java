package org.motechproject.web.message.converters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CSVHttpMessageConverterTest {
    @Mock
    private HttpInputMessage httpInputMessage;
    @Mock
    private ServletServerHttpResponse httpOutputMessage;
    @Mock
    private OutputStream mockedOutputStream;
    private CSVHttpMessageConverter csvHttpMessageConverter;

    @Before
    public void setUp() {
        csvHttpMessageConverter = new CSVHttpMessageConverter();
    }

    @Test
    public void shouldAllowReadOnlyIfMimeTypeIsTextCsvAndHasCSVEntityAnnotation() {
        assertFalse(csvHttpMessageConverter.canRead(InvalidCSVEntity.class, MediaType.APPLICATION_XML));
        assertFalse(csvHttpMessageConverter.canRead(InvalidCSVEntity.class, new MediaType("text", "csv")));
        assertTrue(csvHttpMessageConverter.canRead(ValidCSVEntity.class, new MediaType("text", "csv")));
    }

    @Test
    public void shouldAllowWriteOnlyIfMimeTypeIsTextCsvAndHasCSVEntityAnnotation() {
        assertFalse(csvHttpMessageConverter.canWrite(InvalidCSVEntity.class, MediaType.APPLICATION_XML));
        assertFalse(csvHttpMessageConverter.canWrite(InvalidCSVEntity.class, new MediaType("text", "csv")));
        assertTrue(csvHttpMessageConverter.canWrite(ValidCSVEntity.class, new MediaType("text", "csv")));
    }

    @Test
    public void shouldWriteToCSVStream() throws IOException {
        ValidCSVEntities validCSVEntities = new ValidCSVEntities();
        final ValidCSVEntity validCSVEntity = new ValidCSVEntity();
        validCSVEntity.setSome("a");
        validCSVEntity.setSomeOther("b");
        validCSVEntities.add(validCSVEntity);
        when(httpOutputMessage.getBody()).thenReturn(mockedOutputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        when(httpOutputMessage.getHeaders()).thenReturn(httpHeaders);
        csvHttpMessageConverter.writeInternal(validCSVEntities, httpOutputMessage);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedOutputStream).write(argumentCaptor.capture(), anyInt(), anyInt());

        String actualStreamContent = new String(argumentCaptor.getValue());
        String[] actualStreamContents = actualStreamContent.split("\n");
        assertEquals("some,someOther", actualStreamContents[0]);
        assertEquals("a,b", actualStreamContents[1]);
        assertTrue(httpHeaders.get("Content-Disposition").contains("attachment; filename=test.csv"));

    }

    @Test
    public void shouldConvertCSVStreamToObject() throws IOException {
        String csvText = "some,someOther" + System.lineSeparator() + "a,b";
        InputStream inputStream = new ByteArrayInputStream(csvText.getBytes());
        when(httpInputMessage.getBody()).thenReturn(inputStream);

        List<ValidCSVEntity> validCSVEntities = (List<ValidCSVEntity>) csvHttpMessageConverter.readInternal(ValidCSVEntity.class, httpInputMessage);

        assertEquals(1, validCSVEntities.size());
        ValidCSVEntity validCSVEntity = validCSVEntities.get(0);
        assertEquals("a", validCSVEntity.getSome());
        assertEquals("b", validCSVEntity.getSomeOther());
    }
}


