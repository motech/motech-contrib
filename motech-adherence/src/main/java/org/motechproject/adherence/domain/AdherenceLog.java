package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

import java.util.HashMap;
import java.util.Map;

@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {

    /*The id of the entity for whom adherence is recorded*/
    @JsonProperty
    protected String externalId;
    /*The id of the phenomenon against which adherence is recorded*/
    @JsonProperty
    protected String referenceId;

    @JsonProperty
    protected Integer year;

    @JsonProperty
    protected Map<String, Adherence> dailyRecordOfAdherence;

    protected AdherenceLog() {
        dailyRecordOfAdherence = new HashMap<String, Adherence>();
    }

    public AdherenceLog(String externalId, String referenceId, LocalDate date) {
        this();
        this.externalId = externalId;
        this.referenceId = referenceId;
        this.year = date.getYear();
    }

    public AdherenceLog recordAdherence(LocalDate from, LocalDate to, final int dosesTaken, final int totalDoses) {
        ForEachDay.between(from, to).new Action() {

            @Override
            public void steps(LocalDate date) {
                String dayOfYear = Integer.toString(date.getDayOfYear());
                dailyRecordOfAdherence.put(dayOfYear, new Adherence(dosesTaken, totalDoses));
            }
        }.execute();
        return this;
    }

    public String externalId() {
        return externalId;
    }

    public String referenceId() {
        return referenceId;
    }

    public Integer year() {
        return year;
    }
}