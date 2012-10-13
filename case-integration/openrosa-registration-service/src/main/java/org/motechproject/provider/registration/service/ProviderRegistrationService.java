package org.motechproject.provider.registration.service;

import org.apache.log4j.Logger;
import org.motechproject.casexml.CaseLog;
import org.motechproject.casexml.builder.ResponseMessageBuilder;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationParserException;
import org.motechproject.provider.registration.exception.OpenRosaRegistrationValidationException;
import org.motechproject.provider.registration.parser.RegistrationParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.motechproject.util.DateUtil.now;

public abstract class ProviderRegistrationService<T> {

    private static Logger logger = Logger.getLogger(ProviderRegistrationService.class);
    private ResponseMessageBuilder responseMessageBuilder;
    private Class<T> clazz;
    AllCaseLogs allCaseLogs;

    @Autowired
    public void setResponseMessageBuilder(ResponseMessageBuilder responseMessageBuilder) {
        this.responseMessageBuilder = responseMessageBuilder;
    }

    public ProviderRegistrationService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity<String> processCase(HttpServletRequest request, HttpEntity<String> requestEntity) throws IOException {
        logger.info("Received provider registration request: " + requestEntity.getBody());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        ResponseEntity<String> response;

        RegistrationParser<T> parser = new RegistrationParser<T>(clazz, requestEntity.getBody());
        CaseLog persistedLog = null;
        try {
            T provider = parser.parseProvider();
            createOrUpdate(provider);
            response = new ResponseEntity<String>(responseMessageBuilder.messageForSuccess(), responseHeaders, HttpStatus.OK);
            persistedLog = createNewLog(request.getPathInfo(), requestEntity.getBody(), false);
        } catch (OpenRosaRegistrationParserException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, exception.getStatusCode());
            persistedLog = createNewLog(request.getPathInfo(), requestEntity.getBody(), true);
        } catch (OpenRosaRegistrationValidationException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, exception.getHttpStatusCode());
            persistedLog = createNewLog(request.getPathInfo(), requestEntity.getBody(), true);
        } catch (RuntimeException exception) {
            response = new ResponseEntity<String>(responseMessageBuilder.messageForRuntimeException(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            persistedLog = createNewLog(request.getPathInfo(), requestEntity.getBody(), true);
        }
        logger.info("Response sent: Status Code: " + response.getStatusCode() + ". Body: " + response.getBody());
        persistedLog.setResponse(response.getBody());
        log(persistedLog);
        return response;
    }

    private CaseLog createNewLog(String requestURI, String requestBody, boolean hasException) {
        return new CaseLog(requestBody, requestURI, hasException, now().withMillisOfSecond(0));
    }

    private void log(CaseLog log) {
        allCaseLogs.add(log);
    }

    @Autowired
    public void setAllCaseLogs(AllCaseLogs allCaseLogs) {
        this.allCaseLogs = allCaseLogs;
    }

    public abstract void createOrUpdate(T registration) throws OpenRosaRegistrationValidationException;
}
