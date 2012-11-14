package org.ei.commcare.api.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.Comparator;

public class CommCareFormInstanceSubmittedTimeStampComparator implements Comparator<CommCareFormInstance> {

    private DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();

    @Override
    public int compare(CommCareFormInstance formInstance1, CommCareFormInstance formInstance2) {
        DateTime formInstance1SubmittedTimeStamp = DateTime.parse(formInstance1.submittedTimeStamp());
        DateTime formInstance2SubmittedTimeStamp = DateTime.parse(formInstance2.submittedTimeStamp());

        return dateTimeComparator.compare(formInstance1SubmittedTimeStamp, formInstance2SubmittedTimeStamp);
    }
}
