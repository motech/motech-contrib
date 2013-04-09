package org.motechproject.bigquery.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "query-definitions")
public class NamedQueries {
    @JacksonXmlProperty(localName = "named-queries")
    List<Query> queries;
}
