package org.motechproject.casexml.service;

import org.apache.log4j.Logger;
import org.motechproject.casexml.exception.CaseParserException;
import org.motechproject.casexml.parser.CommcareCaseParser;
import org.motechproject.casexml.service.exception.CaseValidationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

public abstract class CaseService<T> {
    private static Logger logger = Logger.getLogger(CaseService.class);
    private Class<T> clazz;

    public CaseService(Class<T> clazz) {
        this.clazz = clazz;
    }

    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity<String> processCase(HttpEntity<String> requestEntity) throws IOException {
        logger.info(requestEntity.getBody());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);

        try {
            CommcareCaseParser<T> caseParser = new CommcareCaseParser<T>(clazz, requestEntity.getBody());
            T object = caseParser.parseCase();

            if ("CREATE".equals(caseParser.getCaseAction()))
                createCase(object);
            else if ("UPDATE".equals(caseParser.getCaseAction()))
                updateCase(object);
            else if ("CLOSE".equals(caseParser.getCaseAction()))
                closeCase(object);

        } catch (CaseParserException exception) {
            return new ResponseEntity<String>(exception.getMessage(), responseHeaders, HttpStatus.BAD_REQUEST);

        } catch (CaseValidationException exception) {
            return new ResponseEntity<String>(exception.getMessage(), responseHeaders, exception.getStatusCode());

        } catch (RuntimeException exception) {
            return new ResponseEntity<String>(exception.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Request successfully processed.", responseHeaders, HttpStatus.OK);
    }

    public abstract void closeCase(T ccCase) throws CaseValidationException;

    public abstract void updateCase(T ccCase) throws CaseValidationException;

    public abstract void createCase(T ccCase) throws CaseValidationException;

}
