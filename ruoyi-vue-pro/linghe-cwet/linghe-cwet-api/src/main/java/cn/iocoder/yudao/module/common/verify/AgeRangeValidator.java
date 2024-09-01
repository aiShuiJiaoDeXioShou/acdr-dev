package cn.iocoder.yudao.module.common.verify;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
public class AgeRangeValidator implements ConstraintValidator<ValidAgeRange, LocalDateTime> {

    private int minAge;
    private int maxAge;

    @Override
    public void initialize(ValidAgeRange constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
        this.maxAge = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDateTime birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        long age = ChronoUnit.YEARS.between(birthDate, now);
        return !birthDate.isAfter(now) && age >= minAge && age <= maxAge;
    }
}

