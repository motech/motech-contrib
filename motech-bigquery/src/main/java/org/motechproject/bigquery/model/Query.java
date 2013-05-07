package org.motechproject.bigquery.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Query {
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    private String query;
}
