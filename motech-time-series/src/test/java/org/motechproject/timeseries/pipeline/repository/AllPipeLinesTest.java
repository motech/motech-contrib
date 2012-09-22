package org.motechproject.timeseries.pipeline.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.timeseries.pipeline.contract.*;
import org.motechproject.timeseries.pipeline.service.PipeLineRegistration;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertFalse;
import static org.motechproject.util.StringUtil.isNullOrEmpty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-TimeSeries.xml")
public class AllPipeLinesTest {

    @Autowired
    private AllPipeLines allPipeLines;

    @Test
    public void shouldSavePipeLineRegistration() {
        PipeLineRegistration registration = new PipeLineRegistration("externalId", createPipe());
        allPipeLines.add(registration);
        assertFalse(isNullOrEmpty(registration.getId()));
    }

    private PipeLine createPipe() {
        PipeLine pipeLine = new PipeLine();
        pipeLine.baselineValidity(DateUtil.today());
        pipeLine.setName("name");
        pipeLine.setType(createPipeType());
        pipeLine.setTransformations(createTransformations());
        return pipeLine;
    }

    private SimplePipeType createPipeType() {
        SimplePipeType pipeType = new SimplePipeType();
        pipeType.setName("onRecordEvent");
        return pipeType;
    }

    private PipeLineTransformations createTransformations() {
        PipeLineTransformations pipeLineTransformations = new PipeLineTransformations();
        pipeLineTransformations.setHead(createHeadDefinition());
        pipeLineTransformations.setLine(asList(createFunctionDefinition()));
        return pipeLineTransformations;
    }

    private PipeHeadDefinition createHeadDefinition() {
        PipeHeadDefinition pipeHeadDefinition = new PipeHeadDefinition();
        pipeHeadDefinition.setFunction(createFunctionDefinition());
        pipeHeadDefinition.setDataSource(createDataSource());
        return pipeHeadDefinition;
    }

    private FunctionDefinition createFunctionDefinition() {
        FunctionDefinition functionDefinition = new FunctionDefinition();
        functionDefinition.setName("summation");
        return functionDefinition;
    }

    private DataSource createDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setType("point");
        dataSource.queryWith(new String[][]{{"number", "10"}});
        return dataSource;
    }

    @After
    public void tearDown() {
        allPipeLines.removeAll();
    }
}
