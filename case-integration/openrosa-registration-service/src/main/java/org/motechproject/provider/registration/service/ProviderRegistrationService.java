package org.motechproject.provider.registration.service;

import org.motechproject.provider.registration.parser.RegistrationParser;
import org.motechproject.provider.registration.service.exception.OpenRosaRegistrationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public abstract class ProviderRegistrationService<T> {

    private Class<T> clazz;

    public ProviderRegistrationService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity<String> ProcessCase(HttpEntity<String> requestEntity) throws IOException {
        RegistrationParser<T> parser = new RegistrationParser<T>(clazz, requestEntity.getBody());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);

        try {
            T provider = parser.parseProvider();
            createOrUpdate(provider);
        } catch (OpenRosaRegistrationException exception) {
            return new ResponseEntity<String>(exception.getMessage(), responseHeaders, exception.getStatusCode());
        }
        return new ResponseEntity<String>("Request successfully processed.", responseHeaders, HttpStatus.OK);
    }

    public abstract void createOrUpdate(T registration) throws OpenRosaRegistrationException;
}
