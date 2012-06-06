package org.motechproject.importer.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.MappingStrategy;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MyCsvToBean extends CsvToBean {
    @Override
    public List parse(MappingStrategy mapper, CSVReader csv) {
        List returnList = super.parse(mapper, csv);
        List objects = new ArrayList<Object>();
        for (Object o : returnList) {
            if (o != null)
                objects.add(o);
        }
        return objects;
    }

    @Override
    protected Object processLine(MappingStrategy mapper, String[] line) throws IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
        return isValid(line) ? super.processLine(mapper, line) : null;
    }

    private boolean isValid(String[] line) {
        String returnString = "";
        for (String s : line) {
            returnString += s;
        }
        return returnString.trim().isEmpty() ? false : true;
    }
}
