package org.motechproject.web.message.converters;

import org.motechproject.web.message.converters.annotations.CSVFileName;

import java.util.ArrayList;

public class ValidCSVEntities extends ArrayList<ValidCSVEntity> {

    @CSVFileName
    public String getFileName(){
        return "test.csv";
    }
}
