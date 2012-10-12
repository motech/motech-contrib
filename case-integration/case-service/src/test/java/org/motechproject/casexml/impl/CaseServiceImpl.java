package org.motechproject.casexml.impl;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/case/**")
public class CaseServiceImpl extends CaseService<SampleCaseRequest> {


    public CaseServiceImpl() {
        super(SampleCaseRequest.class);
    }

    @Override
    public void closeCase(SampleCaseRequest ccCase) throws CaseException {
    }

    @Override
    public void updateCase(SampleCaseRequest ccCase) throws CaseException {
    }

    @Override
    public void createCase(SampleCaseRequest ccCase) throws CaseException {
    }
}
