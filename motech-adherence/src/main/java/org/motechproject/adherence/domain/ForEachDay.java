package org.motechproject.adherence.domain;

import org.joda.time.LocalDate;
import org.joda.time.Period;

public class ForEachDay {

    private final LocalDate from;
    private final LocalDate to;

    private ForEachDay(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public static ForEachDay between(LocalDate from, LocalDate to) {
        return new ForEachDay(from, to);
    }

    public abstract class Action {

        public abstract void steps(LocalDate date);

        public void execute() {
            Period period = new Period(from, to);
            for (int i = 0; i < period.getDays(); i++) {
                steps(from.plusDays(i));
            }
        }
    }
}
