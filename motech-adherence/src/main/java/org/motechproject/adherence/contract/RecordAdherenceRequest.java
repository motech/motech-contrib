package org.motechproject.adherence.contract;

public class RecordAdherenceRequest {

    private String externalId;

    private String referenceId;

    private int dosesTaken;

    private int totalDoses;

    public String getExternalId() {
        return externalId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public int getDosesTaken() {
        return dosesTaken;
    }

    public int getTotalDoses() {
        return totalDoses;
    }

    public RecordAdherenceRequest addDosesTaken(int dosesTaken) {
        this.dosesTaken += dosesTaken;
        return this;
    }

    public RecordAdherenceRequest addTotalDoses(int totalDoses) {
        this.totalDoses += totalDoses;
        return this;
    }
}
