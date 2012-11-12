package org.motechproject.export.model;

import org.motechproject.export.annotation.ExportValue;

public class DummyCsv {

    private String district;
    private String panchy;

    @ExportValue(column="district", index = 0)
    public String getDistrict() {
        return district;
    }

    @ExportValue(column="panchy", index = 1)
    public String getPanchy() {
        return panchy;
    }

}
