package org.motechproject.web.message.converters;

import org.motechproject.export.annotation.ExportValue;
import org.motechproject.web.message.converters.annotations.CSVEntity;

@CSVEntity
public class ValidCSVEntity {
    private String some;
    private String someOther;

    public ValidCSVEntity() {
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
