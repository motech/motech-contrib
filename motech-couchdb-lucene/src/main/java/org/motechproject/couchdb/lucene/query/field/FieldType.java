package org.motechproject.couchdb.lucene.query.field;

public enum FieldType {
    STRING("string"), DATE("date"), INT("int"), LONG("long"), FLOAT("float"), DOUBLE("double");

    private String value;

    FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
