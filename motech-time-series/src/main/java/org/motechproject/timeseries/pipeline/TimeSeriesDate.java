package org.motechproject.timeseries.pipeline;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.util.DateUtil;

import java.util.regex.Pattern;

import static java.lang.Integer.valueOf;

public class TimeSeriesDate {

    private DateString dateString;

    public TimeSeriesDate(String date) {
        if (Pattern.matches(".*[a-z].*", date)) {
            dateString = new RelativeDate(date);
        } else {
            dateString = new AbsoluteDate(date);
        }
    }

    public DateTime getValue() {
        return dateString.getValue();
    }

    protected abstract class DateString {

        protected String dateString;

        protected DateString(String dateString) {
            this.dateString = dateString;
        }

        protected abstract DateTime getValue();
    }

    protected class AbsoluteDate extends DateString {


        protected AbsoluteDate(String dateString) {
            super(dateString);
        }

        @Override
        protected DateTime getValue() {
            return DateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-mm-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        }
    }

    protected class RelativeDate extends DateString {

        private int years;
        private int months;
        private int days;
        private int weeks;
        private int hours;
        private int minutes;
        private int seconds;

        protected RelativeDate(String dateString) {
            super(dateString);
            parse();
        }

        private void parse() {
            int no = 0;
            int weight = 1;
            for (Character character : dateString.toCharArray()) {
                if (!Character.isLetter(character)) {
                    no = (no * (weight * 10)) + valueOf(character);
                    weight++;
                } else {
                    switch (character) {
                        case 'y':
                            years = no;
                            break;
                        case 'm':
                            months = no;
                            break;
                        case 'w':
                            weeks = no;
                            break;
                        case 'd':
                            days = no;
                            break;
                        case 'h':
                            hours = no;
                            break;
                        case 'i':
                            minutes = no;
                            break;
                        case 's':
                            seconds = no;
                    }
                    no = 0;
                    weight = 1;
                }
            }
        }

        @Override
        protected DateTime getValue() {
            return DateUtil.now().minusYears(years).minusMonths(months).minusWeeks(weeks).minusDays(days).minusHours(hours).minusMinutes(minutes).minusSeconds(seconds).withMillisOfSecond(0);
        }
    }
}
