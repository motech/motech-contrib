package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceSummary;
import org.motechproject.adherence.contract.RecordAdherenceRequest;

public interface AdherenceService {

    public void recordAdherence(RecordAdherenceRequest recordAdherenceRequest);

    public AdherenceSummary summarizeAdherence(String externalId, String referenceId, LocalDate from, LocalDate to);
}
