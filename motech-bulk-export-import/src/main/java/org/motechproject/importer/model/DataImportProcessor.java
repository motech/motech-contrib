package org.motechproject.importer.model;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.Error;
import org.motechproject.importer.domain.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class DataImportProcessor {
    private Object importer;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Method postMethod;
    private Method validateMethod;

    public DataImportProcessor(Object importer) {
        this.importer = importer;
        for (Method method : this.importer.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Post.class)) {
                postMethod = method;
            }
            if (method.isAnnotationPresent(Validate.class)) {
                validateMethod = method;
            }
        }
    }

    public void process(String... filePaths) {
        try {
            List<Object> allValuesFromFile = new ArrayList<>();
            for (String filePath : filePaths) {
                List<Object> valuesFromFile = parse(filePath);
                ValidationResponse validationResponse = validate(valuesFromFile);
                if (validationResponse.isValid()) {
                    allValuesFromFile.addAll(valuesFromFile);
                } else {
                    processErrors(validationResponse.getErrors(), filePath);
                }
            }

            if (!allValuesFromFile.isEmpty()) {
                invokePostMethod(allValuesFromFile);
            }
        } catch (Exception e) {
            System.err.println("Error while importing csv : " + ExceptionUtils.getFullStackTrace(e));
            logger.error("Error while importing csv : " + ExceptionUtils.getFullStackTrace(e));
        }
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
        for (Error error : errors) {
            writer.write(error.getMessage());
            writer.newLine();
        }
        writer.close();
    }

    public abstract String entity();

    public abstract Class bean();

    public abstract List<Object> parse(String filePath) throws Exception;
}
