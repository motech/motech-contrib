package org.motechproject.provider.registration.impl;

import org.motechproject.provider.registration.SampleProviderRequest;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/case/**")
public class SampleProviderRegistrationService extends ProviderRegistrationService<SampleProviderRequest> {


    public SampleProviderRegistrationService() {
        super(SampleProviderRequest.class);
    }

    @Override
    public void createOrUpdate(SampleProviderRequest registration) throws OpenRosaRegistrationValidationException {
    }


}
