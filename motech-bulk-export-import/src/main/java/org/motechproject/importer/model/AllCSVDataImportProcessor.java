package org.motechproject.importer.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class AllCSVDataImportProcessor implements BeanPostProcessor {

    private Map<String, CSVDataImportProcessor> dataImportProcessors = new Hashtable<String, CSVDataImportProcessor>();

    public CSVDataImportProcessor get(String groupName) {
        return dataImportProcessors.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (CSVDataImportProcessor.isValid(bean.getClass())) {
            CSVDataImportProcessor dataImportProcessor = new CSVDataImportProcessor(bean);
            dataImportProcessors.put(dataImportProcessor.entity(), dataImportProcessor);
        }
        return bean;
    }
}
