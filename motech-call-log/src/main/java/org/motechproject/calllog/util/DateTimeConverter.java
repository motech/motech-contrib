package org.motechproject.calllog.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;

public class DateTimeConverter {
    public static final String DATE_TIME_FORMAT = "dd/MM/YYYY HH:mm:ss";

    private DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    private DateTime date;

    public DateTimeConverter(DateTime date) {
        this.date = date;
    }

    public DateTimeConverter(String date) {
        if (StringUtils.isNotBlank(date))
            this.date = formatter.parseDateTime(date);
    }

    public static DateTimeConverter datetime(String date) {
        return new DateTimeConverter(date);
    }

    public static DateTimeConverter date(DateTime date) {
        return new DateTimeConverter(date);
    }

    public String value() {
        if (null == date) {
            return "";
        }
        return date.toString(DATE_TIME_FORMAT);
    }

    public DateTime date() {
        return date;
    }

    public Timestamp time() {
        return new Timestamp(date.toDate().getTime());
    }

    public static Timestamp timestamp(String time) {
        if(StringUtils.isBlank(time)){
            return null;
        }
        return datetime(time).time();
    }
}
