package org.motechproject.importer.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class AllExcelDataImportProcessor implements BeanPostProcessor {

    private Map<String, ExcelDataImportProcessor> dataImportProcessors = new Hashtable<String, ExcelDataImportProcessor>();

    public ExcelDataImportProcessor get(String groupName) {
        return dataImportProcessors.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ExcelDataImportProcessor.isValid(bean.getClass())) {
            ExcelDataImportProcessor dataImportProcessor = new ExcelDataImportProcessor(bean);
            dataImportProcessors.put(dataImportProcessor.entity(), dataImportProcessor);
        }
        return bean;
    }
}
