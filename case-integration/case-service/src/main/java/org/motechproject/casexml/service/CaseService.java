package org.motechproject.casexml.service;

import org.apache.log4j.Logger;
import org.motechproject.casexml.exception.CaseParserException;
import org.motechproject.casexml.parser.CommcareCaseParser;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.casexml.service.response.ResponseMessageBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public abstract class CaseService<T> {
    private static Logger logger = Logger.getLogger(CaseService.class);
    private Class<T> clazz;

    private ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();

    public CaseService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity<String> processCase(HttpEntity<String> requestEntity) throws IOException {
        logger.info(requestEntity.getBody());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_XML);

        try {
            CommcareCaseParser<T> caseParser = new CommcareCaseParser<T>(clazz, requestEntity.getBody());
            T object = caseParser.parseCase();

            processCaseAction(caseParser, object);

        } catch (CaseParserException exception) {
            return loggedResponse(new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, HttpStatus.BAD_REQUEST));

        } catch (CaseException exception) {
            return loggedResponse(new ResponseEntity<String>(responseMessageBuilder.createResponseMessage(exception), responseHeaders, exception.getHttpStatusCode()));

        } catch (RuntimeException exception) {
            return loggedResponse(new ResponseEntity<String>(responseMessageBuilder.messageForRuntimeException(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return loggedResponse(new ResponseEntity<String>(responseMessageBuilder.messageForSuccess(), responseHeaders, HttpStatus.OK));
    }

    private ResponseEntity<String> loggedResponse(ResponseEntity<String> responseEntity) {
        if(responseEntity.getStatusCode().equals(HttpStatus.OK))
            logger.info(responseEntity);
        else
            logger.error(responseEntity);
        return responseEntity;
    }

    private void processCaseAction(CommcareCaseParser<T> caseParser, T object) throws CaseException {
        if ("CREATE".equals(caseParser.getCaseAction()))
            createCase(object);
        else if ("UPDATE".equals(caseParser.getCaseAction()))
            updateCase(object);
        else if ("CLOSE".equals(caseParser.getCaseAction()))
            closeCase(object);
    }


    public abstract void closeCase(T ccCase) throws CaseException;

    public abstract void updateCase(T ccCase) throws CaseException;

    public abstract void createCase(T ccCase) throws CaseException;

}
