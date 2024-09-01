package com.yskj.acdr.common.verify;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return false;
        }
        // 正则表达式可以根据需要调整
        return phoneField.matches("^1[3-9]\\d{9}$");
    }
}

