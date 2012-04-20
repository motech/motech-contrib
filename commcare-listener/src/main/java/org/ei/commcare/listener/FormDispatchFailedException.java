package org.ei.commcare.listener;

import java.util.ArrayList;

public class FormDispatchFailedException extends RuntimeException {
    private ArrayList<Throwable> innerExceptions;

    public FormDispatchFailedException() {
        innerExceptions = new ArrayList<Throwable>();
    }

    public void add(Throwable e) {
        innerExceptions().add(e);
    }

    public ArrayList<Throwable> innerExceptions() {
        return innerExceptions;
    }

    public boolean hasExceptions() {
        return innerExceptions.size() > 0;
    }
}
