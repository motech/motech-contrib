package org.motechproject.validation.validator;

import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.validation.validator.root.PropertyValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.apache.commons.lang.StringUtils.isBlank;

@Component
public class NotNullOrEmptyValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, String scope, Errors errors) {
        if (field.isAnnotationPresent(NotNullOrEmpty.class)) {
            String[] specifiedScopes = field.getAnnotation(NotNullOrEmpty.class).scope();
            if (scope == null || specifiedScopes.length == 0 || Arrays.asList(specifiedScopes).contains(scope)) {
                if (isInValid(target, field)) {
                    errors.rejectValue(field.getName(), "invalid-data", "value should not be null");
                }
            }
        }
    }

    private boolean isInValid(Object target, Field field) {
        field.setAccessible(true);
        try {
            Object value = field.get(target);
            return value == null || isBlank(value.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

}
