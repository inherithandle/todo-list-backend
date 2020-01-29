package com.gtchoi.todolistbackend.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VariableNamingPolicyValidator implements ConstraintValidator<VariableNamingPolicy, String> {


    @Override
    public void initialize(VariableNamingPolicy constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return false;
        }
        return value.matches("[a-zA-Z][a-zA-Z0-9_]*");
    }
}
