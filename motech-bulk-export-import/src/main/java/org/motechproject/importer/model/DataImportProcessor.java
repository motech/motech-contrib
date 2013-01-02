package org.motechproject.importer.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.CSVImportResponse;
import org.motechproject.importer.domain.Error;
import org.motechproject.importer.domain.ValidationException;
import org.motechproject.importer.domain.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class DataImportProcessor {
    private Object importer;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Method postMethod;
    private Method validateMethod;
    protected Class bean;

    protected DataImportProcessor(Class bean) {
        this.bean = bean;
    }

    public DataImportProcessor(Object importer, Class bean) {
        this.importer = importer;
        this.bean = bean;
        for (Method method : this.importer.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Post.class)) {
                postMethod = method;
            }
            if (method.isAnnotationPresent(Validate.class)) {
                validateMethod = method;
            }
        }
    }

    public CSVImportResponse process(String... filePaths) {
        String lastRunFilePath = StringUtils.EMPTY;
        try {
            for (String filePath : filePaths) {
                ValidationResponse validationResponse = process(new FileReader(filePath));

                if (!validationResponse.isValid()) {
                    processErrors(validationResponse.getErrors(), filePath);
                    throw new ValidationException();
                }
                lastRunFilePath = new File(filePath).getName();
            }
        } catch (Exception e) {
            System.err.println("Error while importing csv : " + ExceptionUtils.getFullStackTrace(e));
            logger.error("Error while importing csv : " + ExceptionUtils.getFullStackTrace(e));
            return new CSVImportResponse(lastRunFilePath, false);
        }
        return new CSVImportResponse(lastRunFilePath, true);
    }

    private ValidationResponse process(Reader reader) throws Exception {
        List<Object> valuesFromFile = parse(reader);
        ValidationResponse validationResponse = validate(valuesFromFile);
        if (!validationResponse.isValid()) {
            return validationResponse;
        }
        invokePostMethod(valuesFromFile);
        return validationResponse;
    }

    public String processContent(String content) throws Exception {
        ValidationResponse validationResponse = process(new StringReader(content));

        if (validationResponse.isValid()) {
            return null;
        }

        StringWriter stringWriter = new StringWriter();
        processErrors(validationResponse.getErrors(), stringWriter);
        return stringWriter.toString();
    }

    private void invokePostMethod(List<Object> entities) throws IllegalAccessException, InvocationTargetException {
        if (postMethod != null)
            postMethod.invoke(importer, entities);
    }

    private ValidationResponse validate(List<Object> valuesFromFile) throws IllegalAccessException, InvocationTargetException {
        ValidationResponse validationResponse = new ValidationResponse(true);
        if (validateMethod != null) {
            validationResponse = (ValidationResponse) validateMethod.invoke(importer, valuesFromFile);
        }
        return validationResponse;
    }

    private void processErrors(List<Error> errors, String filePath) throws IOException {
        String fileDirectory = new File(new File(filePath).getAbsolutePath()).getParent();
        File errorsFile = new File(fileDirectory + File.separator + "errors.csv");
        errorsFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(errorsFile));
        processErrors(errors, writer);
    }

    private void processErrors(List<Error> errors, Writer writer) throws IOException {
        for (Error error : errors) {
            writer.write(error.getMessage());
            writer.write("\n");
        }
        writer.close();
    }

    public abstract String entity();

    public abstract List<Object> parse(Reader reader) throws Exception;
}
