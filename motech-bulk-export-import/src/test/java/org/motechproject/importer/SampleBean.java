package org.motechproject.importer;

import org.motechproject.importer.annotation.ColumnName;

public class SampleBean {

    @ColumnName(name = "y")
    private String sampleY;

    private String sampleX;

    private String notPresentInFile;

    @ColumnName(name = "setUsingSetter")
    private String shouldBeSetUsingSetter;

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

    public String getNotPresentInFile() {
        return notPresentInFile;
    }

    public void setNotPresentInFile(String notPresentInFile) {
        this.notPresentInFile = notPresentInFile;
    }

    public String getShouldBeSetUsingSetter() {
        return shouldBeSetUsingSetter;
    }

    @ColumnName(name = "setUsingSetter")
    public void setShouldBeSetUsingSetter(String shouldBeSetUsingSetter) {
        this.shouldBeSetUsingSetter = "differentValueThanTheOnePresentInCSVFile";
    }
}
