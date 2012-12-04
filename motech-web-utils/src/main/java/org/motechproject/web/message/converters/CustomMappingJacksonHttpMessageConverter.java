package org.motechproject.web.message.converters;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class CustomMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {

    public CustomMappingJacksonHttpMessageConverter() {
        this("NON_NULL");
    }

    public CustomMappingJacksonHttpMessageConverter(String inclusion) {
        super();
        setSerializationInclusion(JsonSerialize.Inclusion.valueOf(inclusion));
    }

    private void setSerializationInclusion(JsonSerialize.Inclusion inclusion) {
        this.getObjectMapper().setSerializationInclusion(inclusion);
    }
}
