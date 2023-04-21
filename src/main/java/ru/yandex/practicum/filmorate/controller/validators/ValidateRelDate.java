package ru.yandex.practicum.filmorate.controller.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ValidateRelDate implements ConstraintValidator<DateReleaseValidator, LocalDate> {
    static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    @Override
    public void initialize (DateReleaseValidator dateReleaseValidator){

    }
    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDate == null || !releaseDate.isBefore(MIN_DATE_RELEASE);
    }
}
