package org.motechproject.provider.registration;

import org.motechproject.provider.registration.contract.OpenRosaXmlRequest;

public class SampleProviderRequest implements OpenRosaXmlRequest {
    @Override
    public String getId() {
        return "sample Id";
    }

    @Override
    public String getType() {
        return "sample type";
    }
}
