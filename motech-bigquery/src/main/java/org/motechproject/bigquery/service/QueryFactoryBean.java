package org.motechproject.bigquery.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.motechproject.bigquery.model.NamedQueries;
import org.motechproject.bigquery.model.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamSource;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueryFactoryBean {

    @Resource(name = "querySource")
    private InputStreamSource resource;

    @Bean
    AllQueries loadQueries(InputStreamSource resource) {
        XmlMapper xmlMapper = new XmlMapper();
        NamedQueries namedQueries;
        try {
            namedQueries = xmlMapper.readValue(resource.getInputStream(), NamedQueries.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getQueries(namedQueries);
    }

    private AllQueries getQueries(NamedQueries namedQueries) {
        Map<String, String> queries = new HashMap<>();
        for(Query query : namedQueries.getQueries()){
            queries.put(query.getName(), query.getQuery());
        }
        return new AllQueries(queries);
    }
}
