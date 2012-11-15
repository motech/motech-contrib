package org.motechproject.couchdb.lucene.query.field;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum FieldType {
    STRING("string"),
    DATE("date") {
        @Override
        public String transform(String fieldValue) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            return formatter.parseLocalDate(fieldValue).toString("yyyy-MM-dd");
        }
    },
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double");

    private String value;

    FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String transform(String fieldValue){
        return fieldValue;
    }

}
