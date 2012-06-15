package org.motechproject.provider.registration.service;

import org.apache.log4j.Logger;
import org.motechproject.casexml.builder.ResponseMessageBuilder;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationParserException;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.parser.RegistrationParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public abstract class ProviderRegistrationService<T> {

    private static Logger logger = Logger.getLogger(ProviderRegistrationService.class);
    private ResponseMessageBuilder responseMessageBuilder;
    private Class<T> clazz;

    @Autowired
    public void setResponseMessageBuilder(ResponseMessageBuilder responseMessageBuilder) {
        this.responseMessageBuilder = responseMessageBuilder;
    }

    public ProviderRegistrationService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity<String> processCase(HttpEntity<String> requestEntity) throws IOException {
        logger.info("Received provider registration request: " + requestEntity.getBody());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        ResponseEntity<String> response;

        RegistrationParser<T> parser = new RegistrationParser<T>(clazz, requestEntity.getBody());
        try {
            T provider = parser.parseProvider();
            createOrUpdate(provider);
            response = new ResponseEntity<String>(responseMessageBuilder.messageForSuccess(), responseHeaders, HttpStatus.OK);
        } catch (OpenRosaRegistrationParserException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, exception.getStatusCode());
        } catch (OpenRosaRegistrationValidationException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, exception.getHttpStatusCode());
        } catch (RuntimeException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.messageForRuntimeException(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Response sent: Status Code: " + response.getStatusCode() + ". Body: " + response.getBody());
        return response;
    }

    public abstract void createOrUpdate(T registration) throws OpenRosaRegistrationValidationException;
}
