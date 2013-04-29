package org.motechproject.crud.controller;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.crud.service.CrudEntity;
import org.motechproject.crud.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class CrudController {
    private CrudService crudService;
    private ObjectMapper objectMapper;
    protected Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    public CrudController(CrudService crudService) {
        this.crudService = crudService;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/crud/{entity}/list")
    public String list(@PathVariable("entity") String entityName, Model model){
        CrudEntity entity = crudService.getCrudEntity(entityName);

        model.addAttribute("entity", entityName);
        model.addAttribute("displayName", entity.getDisplayName());
        model.addAttribute("displayFields", entity.getDisplayFields());
        model.addAttribute("filterFields", entity.getFilterFields());
        model.addAttribute("hiddenFields", entity.getHiddenFields());
        model.addAttribute("defaultValues", entity.getDefaultValues());
        model.addAttribute("idField", entity.getIdFieldName());
        return "crud/list";
    }

    @RequestMapping(value = "/crud/{entity}/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("entity") String entityName, @PathVariable("id") String entityId){
        crudService.deleteEntity(entityName, entityId);
    }

    @RequestMapping(value = "/crud/{entity}/get/{id}")
    @ResponseBody
    public Object get(@PathVariable("entity") String entityName, @PathVariable("id") String entityId){
        return crudService.getEntity(entityName, entityId);
    }

    @RequestMapping(value = "/crud/{entity}/save")
    @ResponseBody
    public void save(@PathVariable("entity") String entityName, @RequestBody String entityJson) throws IOException {
        CrudEntity crudEntity = crudService.getCrudEntity(entityName);
        Object object = objectMapper.readValue(entityJson, crudEntity.getEntityType());
        crudService.saveEntity(entityName, object);
    }

    @ResponseBody
    @RequestMapping(value = "/crud/{entity}/schema")
    public String schema(@PathVariable("entity") String entityName) throws JsonMappingException {
        Class entityType = crudService.getCrudEntity(entityName).getEntityType();
        return objectMapper.generateJsonSchema(entityType).toString();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        logger.error("Error occurred", ex);
    }

}