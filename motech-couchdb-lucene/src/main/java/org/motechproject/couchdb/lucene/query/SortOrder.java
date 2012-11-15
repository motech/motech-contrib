package org.motechproject.couchdb.lucene.query;

import lombok.Getter;

public enum SortOrder {
    ASC("/"), DESC("\\");

    @Getter
    private String luceneSortPrefix;

    SortOrder(String luceneSortPrefix) {
        this.luceneSortPrefix = luceneSortPrefix;
    }

    public static SortOrder getSortOrder(String sortOrder){
        return sortOrder.equalsIgnoreCase(DESC.name()) ? DESC : ASC;
    }
}
