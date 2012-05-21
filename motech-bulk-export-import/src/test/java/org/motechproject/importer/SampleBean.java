package org.motechproject.importer;

import org.motechproject.importer.annotation.ColumnName;

public class SampleBean {

    @ColumnName(name = "y")
    private String sampleY;

    private String sampleX;

    public String getSampleY() {
        return sampleY;
    }

    public void setSampleY(String sampleY) {
        this.sampleY = sampleY;
    }

    public String getSampleX() {
        return sampleX;
    }

    public void setSampleX(String sampleX) {
        this.sampleX = sampleX;
    }
}
