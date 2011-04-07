/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2011 Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, nor its respective contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA AND ITS CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION USA OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.motechproject.server.appointmentreminder.service;

import org.motechproject.appointmentreminder.dao.PatientDAO;
import org.motechproject.appointmentreminder.model.Appointment;
import org.motechproject.appointmentreminder.model.Patient;
import org.motechproject.appointmentreminder.model.Visit;
import org.motechproject.model.InitiateCallData;
import org.motechproject.server.service.ivr.IVRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 *
 */
public class AppointmentReminderServiceImpl implements AppointmentReminderService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IVRService ivrService;

    @Autowired
    PatientDAO patientDao
            ;
    //Interim implementation
    String  appointmentReminderVmlUrl = "http://10.0.1.29:8080/TamaIVR/reminder/doc";

    int timeOut;
    public final static String SCHEDULE_APPOINTMENT_REMINDER = "ScheduleAppointmentReminder";

    @Override
    public void remindPatientAppointment(String appointmentId) {

        //TODO - handle DAO exceptions
        Appointment appointment = patientDao.getAppointment(appointmentId);
        Patient patient = patientDao.get(appointment.getPatientId());

        long messageId = 1;
        String phone = patient.getPhoneNumber();

        Date today = setTimeToMidnight(new Date());

        // Patient is in window
        Date reminderWindowStart = setTimeToMidnight(appointment.getReminderWindowStart());
        Date reminderWindowEnd = setTimeToMidnight(appointment.getReminderWindowEnd());
        boolean inWindow = false;
        boolean visitedClinic = false;

        if (reminderWindowStart.compareTo(today) <= 0 &&
                reminderWindowEnd.compareTo(today) >= 0) {
            inWindow = true;
        }

        Set<Visit> visits = patient.getVisits();
        for (Visit v : visits) {
            Date visitDate = setTimeToMidnight(v.getVisitDate());
            if (reminderWindowStart.compareTo(visitDate) <= 0 &&
                    reminderWindowEnd.compareTo(visitDate) >= 0) {
                visitedClinic = true;
            }
        }

        if (!inWindow) {
            log.info("Ignoring reminder event for patientId=" + patient.getClinicPatientId() +
                             "appointmentId=" + appointmentId +
                             " outside of window start=" + reminderWindowStart +
                             " today=" + today + " end=" + reminderWindowEnd);
        }

        if (visitedClinic) {
            log.info("Ignoring reminder event for patientId=" + patient.getClinicPatientId() +
                             "appointmentId=" + appointmentId +
                             " already visited clinic");
        }

        if (inWindow && !visitedClinic) {
            InitiateCallData initiateCallData = new InitiateCallData(messageId, phone,
                                                                     timeOut, appointmentReminderVmlUrl);

            ivrService.initiateCall(initiateCallData);
        }
    }

    private Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
