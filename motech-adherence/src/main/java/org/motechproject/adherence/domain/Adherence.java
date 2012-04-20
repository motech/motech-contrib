package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public final class Adherence {

    @JsonProperty
    protected Integer dosesTaken;

    @JsonProperty
    protected Integer totalDoses;

    private Adherence() {
    }

    public Adherence(Integer dosesTaken, Integer totalDoses) {
        this.dosesTaken = dosesTaken;
        this.totalDoses = totalDoses;
    }
}
