package org.motechproject.validation.validator;

import org.motechproject.validation.validator.root.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Set;

@Component
public class FieldValidator {

    protected Set<PropertyValidator> fieldValidators;

    @Autowired
    public FieldValidator(Set<PropertyValidator> fieldValidators) {
        this.fieldValidators = fieldValidators;
    }

    public void validateField(Object target,
                              String scope,
                              Errors errors,
                              Field field,
                              BeanValidator memberInstanceValidator) {

        field.setAccessible(true);
        if (isValidatedMemberInstance(field)) {
            validateMemberInstance(target, scope, errors, field, memberInstanceValidator);
        } else {
            for (PropertyValidator fieldValidator : fieldValidators) {
                fieldValidator.validateField(target, field, scope, errors);
            }
        }
    }

    private void validateMemberInstance(Object target,
                                        String scope,
                                        Errors errors,
                                        Field field,
                                        BeanValidator memberInstanceValidator) {
        Object fieldValue = null;
        try {
            fieldValue = field.get(target);
            errors.pushNestedPath(field.getName());
            memberInstanceValidator.validate(fieldValue, scope, errors);
            errors.popNestedPath();
        } catch (IllegalAccessException e) {
        }
    }

    private boolean isValidatedMemberInstance(Field field) {
        return field.isAnnotationPresent(Valid.class);
    }
}
