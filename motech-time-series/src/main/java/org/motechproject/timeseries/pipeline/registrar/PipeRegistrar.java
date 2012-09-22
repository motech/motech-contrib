package org.motechproject.timeseries.pipeline.registrar;

import org.motechproject.timeseries.pipeline.contract.PipeLine;

public interface PipeRegistrar {

    public RegistrationResult register(String externalId, PipeLine pipeLine);
}
