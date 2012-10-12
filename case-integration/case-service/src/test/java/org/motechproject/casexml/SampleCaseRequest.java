package org.motechproject.casexml;

import org.motechproject.casexml.contract.CaseXmlRequest;

public class SampleCaseRequest implements CaseXmlRequest {
    @Override
    public String getId() {
        return "Sample Case ID";
    }

    @Override
    public String getType() {
        return "Sample Case Type";
    }
}
