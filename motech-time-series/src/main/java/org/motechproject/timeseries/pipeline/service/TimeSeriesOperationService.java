package org.motechproject.timeseries.pipeline.service;

import org.motechproject.timeseries.pipeline.contract.PipeLine;
import org.motechproject.timeseries.pipeline.registrar.PipeRegistrar;
import org.motechproject.timeseries.pipeline.registrar.RegistrationResult;
import org.motechproject.timeseries.pipeline.repository.AllPipeLines;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSeriesOperationService {

    private List<PipeRegistrar> registrars;
    private AllPipeLines allPipeLines;
    private PipeExecutionContext pipeContext;

    @Autowired
    public TimeSeriesOperationService(List<PipeRegistrar> registrars, AllPipeLines allPipeLines, PipeExecutionContext pipeContext) {
        this.registrars = registrars;
        this.allPipeLines = allPipeLines;
        this.pipeContext = pipeContext;
    }

    public void registerForPipeLine(String externalId, PipeLine pipeLine) {
        pipeLine.defineBaseLineValidity(DateUtil.today());
        for (PipeRegistrar registrar : registrars) {
            RegistrationResult result = registrar.register(externalId, pipeLine);
            if (result == RegistrationResult.success) {
                break;
            }
        }
    }

    public void triggerPipes(String externalId, String eventName) {
        List<PipeLineRegistration> registrations = allPipeLines.withExternalIdAndEvent(externalId, eventName);
        for (PipeLineRegistration registration : registrations) {
            pipeContext.executePipeLine(externalId, registration.getPipeLine());
        }
    }
}
