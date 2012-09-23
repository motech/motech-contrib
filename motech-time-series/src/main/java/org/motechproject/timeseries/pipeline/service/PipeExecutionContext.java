package org.motechproject.timeseries.pipeline.service;

import org.motechproject.timeseries.domain.collection.AllTimeSeriesRecords;
import org.motechproject.timeseries.domain.entity.TimeSeriesRecord;
import org.motechproject.timeseries.pipeline.PipeTransformation;
import org.motechproject.timeseries.pipeline.TimeSeriesSet;
import org.motechproject.timeseries.pipeline.contract.DataSource;
import org.motechproject.timeseries.pipeline.contract.FunctionDefinition;
import org.motechproject.timeseries.pipeline.contract.PipeHeadDefinition;
import org.motechproject.timeseries.pipeline.contract.PipeLine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PipeExecutionContext implements BeanPostProcessor {

    private Map<String, PipeTransformation> nameToBeanMapping = new HashMap<>();
    private AllTimeSeriesRecords allTimeSeriesRecords;

    @Autowired
    public PipeExecutionContext(AllTimeSeriesRecords allTimeSeriesRecords) {
        this.allTimeSeriesRecords = allTimeSeriesRecords;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if (bean instanceof PipeTransformation) {
            PipeTransformation transformation = (PipeTransformation) bean;
            nameToBeanMapping.put(transformation.name(), transformation);
        }
        return bean;
    }

    public TimeSeriesSet executePipeLine(String externalId, PipeLine pipeLine) {
        TimeSeriesRecord record = allTimeSeriesRecords.withExternalId(externalId);
        PipeHeadDefinition head = pipeLine.getTransformations().getHead();

        return executeLine(pipeLine.getTransformations().getLine(), executeHead(record, head));
    }

    private TimeSeriesSet executeHead(TimeSeriesRecord record, PipeHeadDefinition head) {
        return executeFunction(head.getFunction(), constructTimeSeriesSet(record, head));
    }

    private TimeSeriesSet executeLine(List<FunctionDefinition> line, TimeSeriesSet set) {
        TimeSeriesSet currentWorkingSet = set;
        for (FunctionDefinition definition : line) {
            currentWorkingSet = executeFunction(definition, currentWorkingSet);
        }
        return currentWorkingSet;
    }

    private TimeSeriesSet executeFunction(FunctionDefinition function, TimeSeriesSet data) {
        PipeTransformation transformation = nameToBeanMapping.get(function.getName());
        return data.apply(transformation, function.getConfiguration());
    }

    private TimeSeriesSet constructTimeSeriesSet(TimeSeriesRecord record, PipeHeadDefinition head) {
        TimeSeriesSet timeSeriesSet = new TimeSeriesSet();
        List<DataSource> dataSources = head.getDataSources();
        for (DataSource dataSource : dataSources) {
            timeSeriesSet.addRow(dataSource.queryOn(record));
        }
        return timeSeriesSet;
    }
}
