package org.motechproject.timeseries.domain.valueobject;

import java.util.Collections;
import java.util.List;

public class SingularResult {

    private boolean isValid;
    private List<DataPoint> result;

    public SingularResult(boolean valid, List<DataPoint> result) {
        isValid = valid;
        this.result = result;
    }

    public SingularResult(boolean valid) {
        this.isValid = valid;
        result = Collections.emptyList();
    }

    public boolean isValid() {
        return isValid;
    }

    public List<DataPoint> getResult() {
        return result;
    }
}