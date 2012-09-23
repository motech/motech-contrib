package org.motechproject.timeseries.pipeline.registrar;

import org.motechproject.timeseries.pipeline.contract.PipeLine;
import org.motechproject.timeseries.pipeline.repository.AllPipeLines;
import org.motechproject.timeseries.pipeline.service.PipeLineRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecordEventPipeRegistrar implements PipeRegistrar {

    private AllPipeLines allPipeLines;

    @Autowired
    public RecordEventPipeRegistrar(AllPipeLines allPipeLines) {
        this.allPipeLines = allPipeLines;
    }

    @Override
    public RegistrationResult register(String externalId, PipeLine pipeLine) {
        if (pipeLine.getType().getName().equalsIgnoreCase("onRecordEvent")) {
            allPipeLines.add(new PipeLineRegistration(externalId, pipeLine));
            return RegistrationResult.success;
        }
        return RegistrationResult.notApplicable;
    }
}
