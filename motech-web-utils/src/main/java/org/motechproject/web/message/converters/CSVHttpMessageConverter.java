package org.motechproject.web.message.converters;


import org.motechproject.export.util.ReflectionUtil;
import org.motechproject.export.writer.CSVWriter;
import org.motechproject.importer.model.CSVDataImportProcessor;
import org.motechproject.web.message.converters.annotations.CSVEntity;
import org.motechproject.web.message.converters.annotations.CSVFileName;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

public class CSVHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    public CSVHttpMessageConverter() {
        super(new MediaType("text", "csv", Charset.forName("UTF-8")));
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return clazz.isAnnotationPresent(CSVEntity.class) && canRead(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.isAnnotationPresent(CSVEntity.class) && canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        CSVDataImportProcessor csvDataImportProcessor = new CSVDataImportProcessor(clazz);
        return csvDataImportProcessor.parse(new InputStreamReader(inputMessage.getBody()));
    }

    @Override
    public void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String fileName = (String) ReflectionUtil.invokeAnnotatedMethod(o, CSVFileName.class);
        if (fileName != null)
            outputMessage.getHeaders().add("Content-Disposition", "attachment; filename=" + fileName);

        CSVWriter csvWriter = new CSVWriter();
        csvWriter.writeCSVFromData(new OutputStreamWriter(outputMessage.getBody()), (List) o);
    }

}
