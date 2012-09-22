package org.motechproject.timeseries.pipeline.service;

import org.motechproject.timeseries.pipeline.contract.PipeLine;
import org.motechproject.timeseries.pipeline.registrar.PipeRegistrar;
import org.motechproject.timeseries.pipeline.registrar.RegistrationResult;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSeriesOperationService {

    private List<PipeRegistrar> registrars;

    @Autowired
    public TimeSeriesOperationService(List<PipeRegistrar> registrars) {
        this.registrars = registrars;
    }

    public void registerForPipeLine(String externalId, PipeLine pipeLine) {
        pipeLine.baselineValidity(DateUtil.today());
        for (PipeRegistrar registrar : registrars) {
            RegistrationResult result = registrar.register(externalId, pipeLine);
            if (result == RegistrationResult.success) {
                break;
            }
        }
    }
}
