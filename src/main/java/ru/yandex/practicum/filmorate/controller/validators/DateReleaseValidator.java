package ru.yandex.practicum.filmorate.controller.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidateRelDate.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateReleaseValidator {
    String message() default "Дата релиза фильма меньше минимальной";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
