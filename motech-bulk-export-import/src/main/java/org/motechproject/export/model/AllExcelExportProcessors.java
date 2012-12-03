package org.motechproject.export.model;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

import static org.motechproject.export.model.ExcelExportProcessor.isValidDataSource;

@Component
public class AllExcelExportProcessors implements BeanPostProcessor {

    private Map<String, ExcelExportProcessor> reportDataSources = new Hashtable<String, ExcelExportProcessor>();

    public AllExcelExportProcessors() {
    }

    public ExcelExportProcessor get(String groupName) {
        return reportDataSources.get(groupName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isValidDataSource(bean.getClass())) {
            ExcelExportProcessor exportProcessor = new ExcelExportProcessor(bean);
            reportDataSources.put(exportProcessor.name(), exportProcessor);
        }
        return bean;
    }
}
