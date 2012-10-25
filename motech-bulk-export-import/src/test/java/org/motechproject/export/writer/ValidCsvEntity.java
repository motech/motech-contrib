package org.motechproject.export.writer;

import org.motechproject.export.annotation.ExportValue;

public class ValidCsvEntity {
    private String some;
    private String someOther;

    public ValidCsvEntity(String some, String someOther) {
        this.some = some;
        this.someOther = someOther;
    }

    @ExportValue(column = "some", index = 0)
    public String getSome() {
        return some;
    }

    public void setSome(String some) {
        this.some = some;
    }

    @ExportValue(column = "someOther", index = 1)
    public String getSomeOther() {
        return someOther;
    }

    public void setSomeOther(String someOther) {
        this.someOther = someOther;
    }

}
