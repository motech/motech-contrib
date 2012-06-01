package org.motechproject.validation.validator;

import org.motechproject.validation.constraints.Scope;
import org.motechproject.validation.constraints.ValidateIfNotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Component
public class BeanValidator {

    private FieldValidator fieldValidator;

    @Autowired
    public BeanValidator(FieldValidator fieldValidator) {
        this.fieldValidator = fieldValidator;
    }

    public void validate(Object target, String scope, Errors errors) {
        if (shouldValidate(target)) {
            validateFields(target, scope, errors);
        }
    }

    private boolean shouldValidate(Object target) {
        return validateIfNotEmptyType(target.getClass()) ? isNotEmpty(target) : true;
    }

    private boolean validateIfNotEmptyType(Class<?> targetType) {
        return targetType.isAnnotationPresent(ValidateIfNotEmpty.class);
    }

    private boolean isNotEmpty(Object target) {
        boolean isEmpty = true;
        for (Field field : target.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(target);
                if (fieldValue instanceof String) {
                    isEmpty &= isEmpty((String) fieldValue);
                } else {
                    isEmpty &= (fieldValue == null);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return !isEmpty;
    }

    private void validateFields(Object target, String scope, Errors errors) {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Scope.class)) {
                List<String> scopeList = Arrays.asList(field.getAnnotation(Scope.class).scope());
                if (scopeList.contains(scope)) {
                    fieldValidator.validateField(target, scope, errors, field, this);
                }
            } else {
                fieldValidator.validateField(target, scope, errors, field, this);
            }
        }
    }
}
