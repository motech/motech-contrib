package org.motechproject.casexml.request;

public class Index {
    private Patient patient;
    private String patientTagName;

    public Index(Patient patient, String patientTagName) {
        this.patient = patient;
        this.patientTagName = patientTagName;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getPatientTagName() {
        return patientTagName;
    }
}
