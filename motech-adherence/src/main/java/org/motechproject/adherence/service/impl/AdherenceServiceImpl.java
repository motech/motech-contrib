package org.motechproject.adherence.service.impl;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceSummary;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.adherence.service.AdherenceService;
import org.springframework.stereotype.Component;

@Component
public class AdherenceServiceImpl implements AdherenceService {

    @Override
    public void recordAdherence(RecordAdherenceRequest recordAdherenceRequest) {
    }

    @Override
    public AdherenceSummary summarizeAdherence(String externalId, String referenceId, LocalDate from, LocalDate to) {
        return null;
    }
}
