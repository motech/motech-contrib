package org.motechproject.importer;

import java.util.List;

public abstract class DataImporter<T> {

    public abstract List<T> parse(String filePath, Class<T> clazz);

    /* override this  for  validation specific to Implementation*/
    public boolean validate(List<T> entities) {
        return true;
    }

    public abstract List<T> transform(List<T> entities);

    public abstract void post(List<T> entities);

    public void process(String filePath, Class<T> clazz) {
        List<T> valuesFromFile = parse(filePath, clazz);
        if (validate(valuesFromFile)) {
            List<T> entities = transform(valuesFromFile);
            post(entities);
        }
    }


}
