package org.motechproject.server.pillreminder.contract;

import java.util.Date;
import java.util.List;

public class PillRegimenRequest {
    private String externalId;
    private Date startDate;
    private Date endDate;
    private int reminderRepeatWindowInHours;
    private int reminderRepeatIntervalInMinutes;
    private List<DosageRequest> dosageContracts;

    public PillRegimenRequest(String externalId, Date startDate, Date endDate, int reminderRepeatWindowInHours, int reminderRepeatIntervalInMinutes, List<DosageRequest> dosageContracts) {
        this.externalId = externalId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderRepeatWindowInHours = reminderRepeatWindowInHours;
        this.reminderRepeatIntervalInMinutes = reminderRepeatIntervalInMinutes;
        this.dosageContracts = dosageContracts;
    }

    public String getExternalId() {
        return externalId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getReminderRepeatWindowInHours() {
        return reminderRepeatWindowInHours;
    }

    public int getReminderRepeatIntervalInMinutes() {
        return reminderRepeatIntervalInMinutes;
    }

    public List<DosageRequest> getDosageContracts() {
        return dosageContracts;
    }
}
