package org.motechproject.retry.util;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class PeriodParser {
    public static PeriodFormatter FORMATTER = new PeriodFormatterBuilder()
            .appendYears().appendSuffix(" year", " years")
            .appendMonths().appendSuffix(" month", " months")
            .appendDays().appendSuffix(" day", " days")
            .appendHours().appendSuffix(" hour", " hours")
            .appendMinutes().appendSuffix(" minute", " minutes")
            .appendSeconds().appendSuffix(" second", " seconds")
            .toFormatter();
}
