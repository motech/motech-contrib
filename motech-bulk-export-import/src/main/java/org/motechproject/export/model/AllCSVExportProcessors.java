package org.motechproject.export.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

import static org.motechproject.export.model.CSVExportProcessor.isValidDataSource;

@Component
public class AllCSVExportProcessors implements BeanPostProcessor {

    private Map<String, CSVExportProcessor> reportDataSources = new Hashtable<String, CSVExportProcessor>();

    public CSVExportProcessor get(String groupName) {
        return reportDataSources.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isValidDataSource(bean.getClass())) {
            CSVExportProcessor exportProcessor = new CSVExportProcessor(bean);
            reportDataSources.put(exportProcessor.name(), exportProcessor);
        }
        return bean;
    }
}
