package org.motechproject.web.message.converters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.ClassUtils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.IOException;

public class CustomJaxb2RootElementHttpMessageConverter extends Jaxb2RootElementHttpMessageConverter {
    public static final String JAXB_FRAGMENT = "jaxb.fragment";
    public static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";

    private boolean ignoreMissingElements = false;
    private boolean includeHeader = false;
    private boolean formattedOutput = true;

    @Override
    protected void writeToResult(Object o, HttpHeaders headers, Result result) throws IOException {
        try {
            Class clazz = ClassUtils.getUserClass(o);
            Marshaller marshaller = getMarshaller(clazz);
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

    @Override
    protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws IOException {
        try {
            Unmarshaller unmarshaller = getUnmarshaller(clazz);
            if (clazz.isAnnotationPresent(XmlRootElement.class)) {
                return unmarshaller.unmarshal(source);
            }
            else {
                JAXBElement jaxbElement = unmarshaller.unmarshal(source, clazz);
                return jaxbElement.getValue();
            }
        }
        catch (UnmarshalException ex) {
            throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);

        }
        catch (JAXBException ex) {
            throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
        }
    }

    private Unmarshaller getUnmarshaller(Class<?> clazz) throws JAXBException {
        Unmarshaller unmarshaller = createUnmarshaller(clazz);
        if(!ignoreMissingElements) {
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        }
        return unmarshaller;
    }

    private Marshaller getMarshaller(Class<?> clazz) throws PropertyException {
        Marshaller marshaller = createMarshaller(clazz);
        marshaller.setProperty(JAXB_FRAGMENT, !includeHeader);
        marshaller.setProperty(JAXB_FORMATTED_OUTPUT, formattedOutput);
        return marshaller;
    }


    private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
        if (contentType != null && contentType.getCharSet() != null) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, contentType.getCharSet().name());
        }
    }

    public void setIgnoreMissingElements(boolean ignoreMissingElements) {
        this.ignoreMissingElements = ignoreMissingElements;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public void setFormattedOutput(boolean formattedOutput) {
        this.formattedOutput = formattedOutput;
    }
}
