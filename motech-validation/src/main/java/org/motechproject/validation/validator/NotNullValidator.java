package org.motechproject.validation.validator;

import org.motechproject.validation.constraints.NotNull;
import org.motechproject.validation.validator.root.PropertyValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class NotNullValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, String scope, Errors errors) {
        if (field.isAnnotationPresent(NotNull.class)) {
            String[] specifiedScopes = field.getAnnotation(NotNull.class).scope();
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
            return field.get(target) == null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

}
