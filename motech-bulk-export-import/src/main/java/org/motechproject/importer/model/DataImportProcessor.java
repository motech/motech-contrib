package org.motechproject.importer.model;

import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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

    public void process(String filePath) {
        try {
            List<Object> valuesFromFile = parse(filePath);
            Boolean isValid = true;
            if(validateMethod != null)
                isValid = (Boolean) validateMethod.invoke(importer, valuesFromFile);
            if (isValid) {
                List<Object> entities = valuesFromFile;
                if(postMethod != null)
                    postMethod.invoke(importer, entities);
            }
        } catch (Exception e) {
            logger.error("Error while importing csv : " + e.getMessage());
        }
    }

    public abstract String entity();

    public abstract Class bean();

    public abstract List<Object> parse(String filePath) throws Exception;
}
