package org.motechproject.validation.validator;

import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.validator.root.PropertyValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnumValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, String scope, Errors errors) {
        if (field.isAnnotationPresent(Enumeration.class)) {
            Enumeration annotation = field.getAnnotation(Enumeration.class);
            Class<? extends Enum> possibleValues = annotation.type();
            boolean validateEmptyString = annotation.validateEmptyString();
            List<String> allEnumerations = allEnumValues(possibleValues);
            if (isInValid(target, field, allEnumerations,validateEmptyString)) {
                errors.rejectValue(field.getName(), "invalid-data", "The value should be one of : " + allEnumerations);
            }
        }
    }

    private boolean isInValid(Object target, Field field, List<String> allEnumerations,boolean validateEmptyString) {
        field.setAccessible(true);
        try {
            if (field.get(target) == null)
                return false;
            String value = ((String) field.get(target)).trim();
            if(validateEmptyString==false && value.isEmpty())
                return false;
            for (String enumValue : allEnumerations)
                if (enumValue.compareToIgnoreCase(value) == 0)
                    return false;
        } catch (Exception ignored) {
        }
        return true;
    }

    private List<String> allEnumValues(Class<? extends Enum> enumerationClass) {
        List<String> allEnumerations = new ArrayList<String>();
        for (Enum value : enumerationClass.getEnumConstants()) {
            allEnumerations.add(value.name());
        }
        return allEnumerations;
    }

}
