package org.motechproject.timeseries.pipeline.registrar;

import org.motechproject.timeseries.pipeline.contract.PipeLine;
import org.motechproject.timeseries.pipeline.repository.AllPipeLines;
import org.motechproject.timeseries.pipeline.service.PipeLineRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CronPipeRegistrar implements PipeRegistrar {

    private AllPipeLines allPipeLines;

    @Autowired
    public CronPipeRegistrar(AllPipeLines allPipeLines) {
        this.allPipeLines = allPipeLines;
    }

    /*TODO: schedule pipe*/
    @Override
    public RegistrationResult register(String externalId, PipeLine pipeLine) {
        if (pipeLine.getType().getName().equalsIgnoreCase("cron")) {
            if (pipeLine.hasParameter("type.expression")) {
                allPipeLines.add(new PipeLineRegistration(externalId, pipeLine));
                return RegistrationResult.success;
            } else {
                return RegistrationResult.insufficientParameters;
            }
        }
        return RegistrationResult.notApplicable;
    }
}
