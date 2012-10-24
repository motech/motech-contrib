package org.motechproject.web.message.converters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.ClassUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.Result;
import java.io.IOException;

public class CustomJaxb2RootElementHttpMessageConverter extends Jaxb2RootElementHttpMessageConverter {
    public static final String JAXB_FRAGMENT = "jaxb.fragment";
    public static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";

    @Override
    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
        try {
            Class clazz = ClassUtils.getUserClass(o);
            Marshaller marshaller = createMarshaller(clazz);
            setNoHeaderOptionOnMarshaller(marshaller);
            setCharset(headers.getContentType(), marshaller);
            marshaller.marshal(o, result);
        }
        catch (MarshalException ex) {
            throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    private void setNoHeaderOptionOnMarshaller(Marshaller marshaller) throws PropertyException {
        marshaller.setProperty(JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }


    private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
        if (contentType != null && contentType.getCharSet() != null) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, contentType.getCharSet().name());
        }
    }
}
