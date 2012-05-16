package org.motechproject.importer;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class CSVImporter<T> extends DataImporter<T> {

    private ColumnPositionMappingStrategy columnPositionMappingStrategy;
    private CsvToBean csvToBean;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected CSVImporter() {
        this.columnPositionMappingStrategy = new ColumnPositionMappingStrategy<T>();
        this.csvToBean = new CsvToBean<T>();
    }

    public List<T> parse(String filePath, Class<T> clazz) {
        List<T> beans = new ArrayList<T>();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath), ',');
            String[] headers = reader.readNext();
            columnPositionMappingStrategy.setType(clazz);
            columnPositionMappingStrategy.setColumnMapping(headers);
            beans = csvToBean.parse(columnPositionMappingStrategy, reader);
        } catch (Exception e) {
            logger.error("Error while importing csv : " + e.getMessage());
        }
        return beans;
    }


}
