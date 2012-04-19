package org.motechproject.provider.registration.service;

import org.motechproject.provider.registration.parser.RegistrationParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Writer;

public abstract class ProviderRegistrationService<T> {

    private Class<T> clazz;

    public ProviderRegistrationService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public void processCase(@RequestBody String xmlDocument, Writer writer) {
        RegistrationParser<T> parser = new RegistrationParser<T>(clazz, xmlDocument);
        T provider = parser.parseProvider();
        createOrUpdate(provider);
    }

    public abstract void createOrUpdate(T registration);
}
